package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ConnectionResetException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.reading.Reader;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.sitemap.SitemapErrorHandler;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.util.location.Locatable;
import org.apache.cocoon.util.location.Location;
import org.apache.cocoon.xml.SaxBuffer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLProducer;
import org.apache.excalibur.source.SourceValidity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.xml.sax.SAXException;

/**
 * This is the base for all implementations of a <code>ProcessingPipeline</code>
 * . It is advisable to inherit from this base class instead of doing a complete
 * own implementation!
 * 
 * @since 2.1
 * @version $Id: AbstractProcessingPipeline.java 606540 2007-12-23 05:44:54Z
 *          vgritsenko $
 */
public class NonCachingPipeline implements ProcessingPipeline, BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonCachingPipeline.class);

    // Error handler stuff
    private SitemapErrorHandler errorHandler;

    private ProcessingPipeline errorPipeline;

    /** True when pipeline has been prepared. */
    private boolean prepared;

    /** The component manager set with compose() and recompose() */
    protected BeanFactory beanFactory;

    /** Configured Expires value */
    protected long configuredExpires;

    /** Configured Output Buffer Size */
    protected int configuredOutputBufferSize;

    /** Expires value */
    protected long expires;

    // Generator stuff
    protected Generator generator;

    protected Parameters generatorParam;

    protected String generatorSource;

    /**
     * This is the last component in the pipeline, either the serializer or a
     * custom XML consumer in case of internal processing.
     */
    protected XMLConsumer lastConsumer;

    /** Output Buffer Size */
    protected int outputBufferSize;

    /** The parameters */
    protected Parameters parameters;

    // Reader stuff
    protected Reader reader;

    protected String readerMimeType;

    protected Parameters readerParam;

    protected String readerSource;

    // Serializer stuff
    protected Serializer serializer;

    protected String serializerMimeType;

    protected Parameters serializerParam;

    protected String serializerSource;

    /** The current SourceResolver */
    protected SourceResolver sourceResolver;

    protected List<Parameters> transformerParams = new LinkedList<Parameters>();

    // Transformer stuff
    protected List<Transformer> transformers = new LinkedList<Transformer>();

    protected List<String> transformerSources = new LinkedList<String>();

    public NonCachingPipeline(final SourceResolver sourceResolver) {
        this.sourceResolver = sourceResolver;
    }

    /**
     * Add a transformer at the end of the pipeline. The transformer role is
     * given : the actual <code>Transformer</code> is fetched from the latest
     * <code>ServiceManager</code>.
     * 
     * @param role
     *            the transformer role in the component manager.
     * @param source
     *            the source used to setup the transformer (e.g. XSL file), or
     *            <code>null</code> if no source is given.
     * @param param
     *            the parameters for the transfomer.
     * @throws ProcessingException
     *             if the generator couldn't be obtained.
     */
    public void addTransformer(final String role, final String source, final Parameters param, final Parameters hintParam)
            throws ProcessingException {
        if (this.reader != null) {
            // Should normally never happen as setting a reader starts pipeline
            // processing
            throw new ProcessingException("Reader already set. Cannot add transformer '" + role + "'", getLocation(param));
        }
        if (this.generator == null) {
            throw new ProcessingException("Must set a generator before adding transformer '" + role + "'", getLocation(param));
        }
        this.transformers.add((Transformer) this.beanFactory.getBean(Transformer.ROLE + '/' + role));
        this.transformerSources.add(source);
        this.transformerParams.add(param);
    }

    /**
     * Get the generator - used for content aggregation
     */
    public Generator getGenerator() {
        return this.generator;
    }

    /**
     * Return the key for the event pipeline If the "event pipeline" (= the
     * complete pipeline without the serializer) is cacheable and valid, return
     * a key. Otherwise return <code>null</code>
     */
    public String getKeyForEventPipeline() {
        return null;
    }

    /**
     * Return valid validity objects for the event pipeline If the
     * "event pipeline" (= the complete pipeline without the serializer) is
     * cacheable and valid, return all validity objects. Otherwise return
     * <code>null</code>
     */
    public SourceValidity getValidityForEventPipeline() {
        return null;
    }

    /**
     * Informs pipeline we have come across a branch point. Default behaviour is
     * do nothing.
     */
    public void informBranchPoint() {
        // this can be overwritten in subclasses
    }

    /**
     * Prepare an internal processing.
     * 
     * @param environment
     *            The current environment.
     * @throws ProcessingException
     */
    public void prepareInternal(final Environment environment) throws ProcessingException {
        this.lastConsumer = null;
        try {
            preparePipeline(environment);
        } catch (ProcessingException e) {
            prepareInternalErrorHandler(environment, e);
        }
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     */
    @SuppressWarnings("unchecked")
    public boolean process(final Environment environment) throws ProcessingException {
        if (!this.prepared) {
            preparePipeline(environment);
        }
        // See if we need to set an "Expires:" header
        if (this.expires != 0) {
            Response res = ObjectModelHelper.getResponse(environment.getObjectModel());
            res.setDateHeader("Expires", System.currentTimeMillis() + this.expires);
            res.setHeader("Cache-Control", "max-age=" + this.expires / 1000 + ", public");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Setting a new Expires object for this resource");
            }
            environment.getObjectModel().put(ObjectModelHelper.EXPIRES_OBJECT,
                    Long.valueOf(this.expires + System.currentTimeMillis()));
        }
        if (this.reader != null) {
            if (checkIfModified(environment, this.reader.getLastModified())) {
                return true;
            }
            return processReader(environment);
        } else {
            // If this is an internal request, lastConsumer was reset!
            if (this.lastConsumer == null) {
                this.lastConsumer = this.serializer;
            }
            connectPipeline(environment);
            return processXMLPipeline(environment);
        }
    }

    /**
     * Process the given <code>Environment</code>, but do not use the
     * serializer. Instead all SAX events are streamed to the XMLConsumer.
     */
    public boolean process(final Environment environment, final XMLConsumer consumer) throws ProcessingException {
        if (this.reader != null) {
            throw new ProcessingException("Streaming of an internal pipeline is not possible with a reader.");
        }
        // Exception happened during setup and was handled
        if (this.errorPipeline != null) {
            return this.errorPipeline.process(environment, consumer);
        }
        // Have to buffer events if error handler is specified.
        SaxBuffer buffer = null;
        this.lastConsumer = this.errorHandler == null ? consumer : (buffer = new SaxBuffer());
        try {
            connectPipeline(environment);
            return processXMLPipeline(environment);
        } catch (ProcessingException e) {
            buffer = null;
            return processErrorHandler(environment, e, consumer);
        } finally {
            if (buffer != null) {
                try {
                    buffer.toSAX(consumer);
                } catch (SAXException e) {
                    throw new ProcessingException("Failed to execute pipeline.", e);
                }
            }
        }
    }

    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Sets error handler for this pipeline. Used for handling errors in the
     * internal pipelines.
     * 
     * @param errorHandler
     *            error handler
     */
    public void setErrorHandler(final SitemapErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Set the generator that will be used as the initial step in the pipeline.
     * The generator role is given : the actual <code>Generator</code> is
     * fetched from the latest <code>ServiceManager</code>.
     * 
     * @param role
     *            the generator role in the component manager.
     * @param source
     *            the source where to produce XML from, or <code>null</code> if
     *            no source is given.
     * @param param
     *            the parameters for the generator.
     * @throws ProcessingException
     *             if the generator couldn't be obtained.
     */
    public void setGenerator(final String role, final String source, final Parameters param, final Parameters hintParam)
            throws ProcessingException {
        if (this.generator != null) {
            throw new ProcessingException("Generator already set. Cannot set generator '" + role + "'", getLocation(param));
        }
        if (this.reader != null) {
            throw new ProcessingException("Reader already set. Cannot set generator '" + role + "'", getLocation(param));
        }
        this.generator = (Generator) this.beanFactory.getBean(Generator.ROLE + '/' + role);
        this.generatorSource = source;
        this.generatorParam = param;
    }

    /**
     * Set the processor's service manager
     */
    public void setProcessorManager(final ServiceManager manager) {
    }

    /**
     * Set the reader for this pipeline
     * 
     * @param mimeType
     *            Can be null
     */
    public void setReader(final String role, final String source, final Parameters param, final String mimeType)
            throws ProcessingException {
        if (this.reader != null) {
            // Should normally never happen as setting a reader starts pipeline
            // processing
            throw new ProcessingException("Reader already set. Cannot set reader '" + role + "'", getLocation(param));
        }
        if (this.generator != null) {
            // Should normally never happen as setting a reader starts pipeline
            // processing
            throw new ProcessingException("Generator already set. Cannot use reader '" + role + "'", getLocation(param));
        }
        this.reader = (Reader) this.beanFactory.getBean(Reader.ROLE + '/' + role);
        this.readerSource = source;
        this.readerParam = param;
        this.readerMimeType = mimeType;
    }

    /**
     * Set the serializer for this pipeline
     * 
     * @param mimeType
     *            Can be null
     */
    public void setSerializer(final String role, final String source, final Parameters param, final Parameters hintParam,
            final String mimeType) throws ProcessingException {
        if (this.serializer != null) {
            // Should normally not happen as adding a serializer starts pipeline
            // processing
            throw new ProcessingException("Serializer already set. Cannot set serializer '" + role + "'", getLocation(param));
        }
        if (this.reader != null) {
            // Should normally never happen as setting a reader starts pipeline
            // processing
            throw new ProcessingException("Reader already set. Cannot set serializer '" + role + "'", getLocation(param));
        }
        if (this.generator == null) {
            throw new ProcessingException("Must set a generator before setting serializer '" + role + "'", getLocation(param));
        }
        this.serializer = (Serializer) this.beanFactory.getBean(Serializer.ROLE + '/' + role);
        this.serializerSource = source;
        this.serializerParam = param;
        this.serializerMimeType = mimeType;
        this.lastConsumer = this.serializer;
    }

    /**
     * Setup this component
     */
    public void setup(final Parameters params) {
        this.parameters = params;
        final String expiresValue = params.getParameter("expires", null);
        if (expiresValue != null) {
            this.expires = parseExpires(expiresValue);
        } else {
            this.expires = this.configuredExpires;
        }
        this.outputBufferSize = params.getParameterAsInteger("outputBufferSize", this.configuredOutputBufferSize);
    }

    /**
     * Parse the expires parameter
     */
    private long parseExpires(final String expire) {
        StringTokenizer tokens = new StringTokenizer(expire);
        // get <base>
        String current = tokens.nextToken();
        if (current.equals("modification")) {
            LOGGER.warn("the \"modification\" keyword is not yet" + " implemented. Assuming \"now\" as the base attribute");
            current = "now";
        }
        if (!current.equals("now") && !current.equals("access")) {
            LOGGER.error("bad <base> attribute, Expires header will not be set");
            return -1;
        }
        long number;
        long modifier;
        long expires = 0;
        while (tokens.hasMoreTokens()) {
            current = tokens.nextToken();
            // get rid of the optional <plus> keyword
            if (current.equals("plus")) {
                current = tokens.nextToken();
            }
            // We're expecting a sequence of <number> and <modification> here
            // get <number> first
            try {
                number = Long.parseLong(current);
            } catch (NumberFormatException nfe) {
                LOGGER.error("state violation: a number was expected here");
                return -1;
            }
            // now get <modifier>
            try {
                current = tokens.nextToken();
            } catch (NoSuchElementException nsee) {
                LOGGER.error("State violation: expecting a modifier" + " but no one found: Expires header will not be set");
            }
            if (current.equals("years")) {
                modifier = 365L * 24L * 60L * 60L * 1000L;
            } else if (current.equals("months")) {
                modifier = 30L * 24L * 60L * 60L * 1000L;
            } else if (current.equals("weeks")) {
                modifier = 7L * 24L * 60L * 60L * 1000L;
            } else if (current.equals("days")) {
                modifier = 24L * 60L * 60L * 1000L;
            } else if (current.equals("hours")) {
                modifier = 60L * 60L * 1000L;
            } else if (current.equals("minutes")) {
                modifier = 60L * 1000L;
            } else if (current.equals("seconds")) {
                modifier = 1000L;
            } else {
                LOGGER.error("Bad modifier (" + current + "): ignoring expires configuration");
                return -1;
            }
            expires += number * modifier;
        }
        return expires;
    }

    protected boolean checkIfModified(final Environment environment, final long lastModified) throws ProcessingException {
        // has the read resource been modified?
        if (!environment.isResponseModified(lastModified)) {
            // environment supports this, so we are finished
            environment.setResponseIsNotModified();
            return true;
        }
        return false;
    }

    /**
     * Sanity check
     * 
     * @return true if the pipeline is 'sane', false otherwise.
     */
    protected boolean checkPipeline() {
        if (this.generator == null && this.reader == null) {
            return false;
        }
        if (this.generator != null && this.serializer == null) {
            return false;
        }
        return true;
    }

    /**
     * Connect the next component
     */
    protected void connect(final Environment environment, final XMLProducer producer, final XMLConsumer consumer)
            throws ProcessingException {
        // Connect next component.
        producer.setConsumer(consumer);
    }

    /**
     * Connect the XML pipeline.
     */
    protected void connectPipeline(final Environment environment) throws ProcessingException {
        XMLProducer prev = this.generator;
        Iterator<Transformer> itt = this.transformers.iterator();
        while (itt.hasNext()) {
            Transformer next = itt.next();
            connect(environment, prev, next);
            prev = next;
        }
        // insert the serializer
        connect(environment, prev, this.lastConsumer);
    }

    protected Location getLocation(final Parameters param) {
        Location location = null;
        if (param instanceof Locatable) {
            location = ((Locatable) param).getLocation();
        }
        if (location == null) {
            location = Location.UNKNOWN;
        }
        return location;
    }

    /**
     * Handles exception which can happen during pipeline processing. If this
     * not a connection reset, then all locations for pipeline components are
     * added to the exception.
     * 
     * @throws ConnectionResetException
     *             if connection reset detected
     * @throws ProcessingException
     *             in all other cases
     */
    protected void handleException(final Exception e) throws ProcessingException {
        // Check if the client aborted the connection
        if (e instanceof SocketException) {
            if (e.getMessage().indexOf("reset") > -1 || e.getMessage().indexOf("aborted") > -1
                    || e.getMessage().indexOf("Broken pipe") > -1 || e.getMessage().indexOf("connection abort") > -1) {
                throw new ConnectionResetException("Connection reset by peer", e);
            }
        } else if (e instanceof IOException) {
            // Tomcat5 wraps SocketException into ClientAbortException which
            // extends IOException.
            if (e.getClass().getName().endsWith("ClientAbortException")) {
                throw new ConnectionResetException("Connection reset by peer", e);
            }
        } else if (e instanceof ConnectionResetException) {
            // Exception comes up from a deeper pipeline
            throw (ConnectionResetException) e;
        }
        // Not a connection reset: add all location information
        if (this.reader == null) {
            // Add all locations in reverse order
            List<Location> locations = new LinkedList<Location>();
            locations.add(getLocation(this.serializerParam));
            for (int i = this.transformerParams.size() - 1; i >= 0; i--) {
                locations.add(getLocation(this.transformerParams.get(i)));
            }
            locations.add(getLocation(this.generatorParam));
            throw ProcessingException.throwLocated("Failed to process pipeline", e, locations);
        } else {
            // Add reader location
            throw ProcessingException.throwLocated("Failed to process reader", e, getLocation(this.readerParam));
        }
    }

    /**
     * @return true if error happened during internal pipeline prepare call.
     */
    protected boolean isInternalError() {
        return this.errorPipeline != null;
    }

    /**
     * If prepareInternal fails, prepare internal error handler.
     */
    protected void prepareInternalErrorHandler(final Environment environment, final ProcessingException ex)
            throws ProcessingException {
        if (this.errorHandler == null) {
            // propagate exception if we have no error handler
            throw ex;
        }
        try {
            this.errorPipeline = this.errorHandler.prepareErrorPipeline(ex);
            if (this.errorPipeline != null) {
                this.errorPipeline.prepareInternal(environment);
            }
        } catch (ProcessingException e) {
            // Log the original exception
            LOGGER.error("Failed to process error handler for exception", ex);
            throw e;
        } catch (Exception e) {
            // Log the original exception
            LOGGER.error("Failed to process error handler for exception", ex);
            throw new ProcessingException("Failed to handle exception <" + ex.getMessage() + ">", e);
        }
    }

    /**
     * Prepare the pipeline
     */
    protected void preparePipeline(final Environment environment) throws ProcessingException {
        if (!checkPipeline()) {
            throw new ProcessingException("Attempted to process incomplete pipeline.");
        }
        if (this.prepared) {
            throw new ProcessingException("Duplicate preparePipeline call caught.");
        }
        if (this.reader != null) {
            setupReader(environment);
        } else {
            setupPipeline(environment);
        }
        this.prepared = true;
    }

    protected boolean processErrorHandler(final Environment environment, final ProcessingException e, final XMLConsumer consumer)
            throws ProcessingException {
        if (this.errorHandler != null) {
            try {
                this.errorPipeline = this.errorHandler.prepareErrorPipeline(e);
                if (this.errorPipeline != null) {
                    this.errorPipeline.prepareInternal(environment);
                    return this.errorPipeline.process(environment, consumer);
                }
            } catch (Exception ignored) {
                LOGGER.debug("Exception in error handler", ignored);
            }
        }
        throw e;
    }

    /**
     * Process the pipeline using a reader.
     * 
     * @throws ProcessingException
     *             if
     */
    protected boolean processReader(final Environment environment) throws ProcessingException {
        try {
            this.setMimeTypeForReader(environment);
            if (this.reader.shouldSetContentLength()) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                this.reader.setOutputStream(os);
                this.reader.generate();
                environment.setContentLength(os.size());
                os.writeTo(environment.getOutputStream(0));
            } else {
                this.reader.setOutputStream(environment.getOutputStream(this.outputBufferSize));
                this.reader.generate();
            }
        } catch (Exception e) {
            handleException(e);
        }
        return true;
    }

    /**
     * Process the SAX event pipeline
     */
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        setMimeTypeForSerializer(environment);
        try {
            if (this.lastConsumer == null) {
                // internal processing
                this.generator.generate();
            } else {
                if (this.serializer.shouldSetContentLength()) {
                    // set the output stream
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    this.serializer.setOutputStream(os);
                    // execute the pipeline:
                    this.generator.generate();
                    environment.setContentLength(os.size());
                    os.writeTo(environment.getOutputStream(0));
                } else {
                    // set the output stream
                    this.serializer.setOutputStream(environment.getOutputStream(this.outputBufferSize));
                    // execute the pipeline:
                    this.generator.generate();
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return true;
    }

    /**
     * Set the mime-type for a reader
     * 
     * @param environment
     *            The current environment
     */
    protected void setMimeTypeForReader(final Environment environment) {
        // Set the mime-type
        // the behaviour has changed from 2.1.x to 2.2 according to bug #10277:
        // MIME type declared in the sitemap (instance or declaration, in this
        // order)
        // Ask the Reader for a MIME type:
        // A *.doc reader could peek into the file
        // and return either text/plain or application/vnd.msword or
        // the reader can use MIME type declared in WEB-INF/web.xml or
        // by the server.
        if (this.readerMimeType != null) {
            // there was a mime-type defined on map:read in the sitemap
            environment.setContentType(this.readerMimeType);
        } else {
            final String mimeType = this.reader.getMimeType();
            if (mimeType != null) {
                environment.setContentType(mimeType);
            }
            // If no mimeType available, leave to to upstream proxy
            // or browser to deduce content-type from URL extension.
        }
    }

    /**
     * Set the mime-type for a serializer
     * 
     * @param environment
     *            The current environment
     */
    protected void setMimeTypeForSerializer(final Environment environment) throws ProcessingException {
        if (this.lastConsumer == null) {
            // internal processing: text/xml
            environment.setContentType("text/xml");
        } else {
            // Set the mime-type
            // the behaviour has changed from 2.1.x to 2.2 according to bug
            // #10277
            if (this.serializerMimeType != null) {
                // there was a serializer defined in the sitemap
                environment.setContentType(this.serializerMimeType);
            } else {
                // ask to the component itself
                String mimeType = this.serializer.getMimeType();
                if (mimeType != null) {
                    environment.setContentType(mimeType);
                } else {
                    // No mimeType available
                    String message = "Unable to determine MIME type for " + environment.getURIPrefix() + "/" + environment.getURI();
                    throw new ProcessingException(message);
                }
            }
        }
    }

    /**
     * Setup pipeline components.
     */
    protected void setupPipeline(final Environment environment) throws ProcessingException {
        try {
            // setup the generator
            this.generator.setup(this.sourceResolver, environment.getObjectModel(), this.generatorSource, this.generatorParam);
            Iterator<Transformer> transformerItt = this.transformers.iterator();
            Iterator<String> transformerSourceItt = this.transformerSources.iterator();
            Iterator<Parameters> transformerParamItt = this.transformerParams.iterator();
            while (transformerItt.hasNext()) {
                Transformer trans = transformerItt.next();
                trans.setup(this.sourceResolver, environment.getObjectModel(), transformerSourceItt.next(),
                        transformerParamItt.next());
            }
            if (this.serializer instanceof SitemapModelComponent) {
                ((SitemapModelComponent) this.serializer).setup(this.sourceResolver, environment.getObjectModel(),
                        this.serializerSource, this.serializerParam);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Setup the reader
     */
    protected void setupReader(final Environment environment) throws ProcessingException {
        try {
            this.reader.setup(this.sourceResolver, environment.getObjectModel(), this.readerSource, this.readerParam);
            // set the expires parameter on the pipeline if the reader is
            // configured with one
            if (this.readerParam.isParameter("expires")) {
                // should this checking be done somewhere else??
                this.expires = this.readerParam.getParameterAsLong("expires");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
}
