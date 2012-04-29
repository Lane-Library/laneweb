package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.sitemap.SitemapErrorHandler;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLProducer;
import org.apache.excalibur.source.SourceValidity;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * This is the base for all implementations of a <code>ProcessingPipeline</code>
 * . It is advisable to inherit from this base class instead of doing a complete
 * own implementation!
 */
public class NonCachingPipeline implements ProcessingPipeline, BeanFactoryAware {

    /** The component manager set with compose() and recompose() */
    private BeanFactory beanFactory;

    /** Configured Expires value */
    private long configuredExpires;

    /** Configured Output Buffer Size */
    private int configuredOutputBufferSize;

    /** Expires value */
    private long expires;

    // Generator stuff
    private Generator generator;

    private Parameters generatorParam;

    private String generatorSource;

    /**
     * This is the last component in the pipeline, either the serializer or a
     * custom XML consumer in case of internal processing.
     */
    private XMLConsumer lastConsumer;

    /** Output Buffer Size */
    private int outputBufferSize;

    /** The parameters */
    private Parameters parameters;

    /** True when pipeline has been prepared. */
    private boolean prepared;

    // Serializer stuff
    private Serializer serializer;

    private Parameters serializerParam;

    private String serializerSource;

    /** The current SourceResolver */
    private SourceResolver sourceResolver;

    private List<Parameters> transformerParams = new LinkedList<Parameters>();

    // Transformer stuff
    private List<Transformer> transformers = new LinkedList<Transformer>();

    private List<String> transformerSources = new LinkedList<String>();

    public NonCachingPipeline(final SourceResolver sourceResolver) {
        this.sourceResolver = sourceResolver;
    }

    /**
     * Add a transformer at the end of the pipeline. The transformer role is
     * given : the actual <code>Transformer</code> is fetched from the
     * <code>BeanFactory</code>.
     * 
     * @param role
     *            the transformer role in the component manager.
     * @param source
     *            the source used to setup the transformer (e.g. XSL file), or
     *            <code>null</code> if no source is given.
     * @param param
     *            the parameters for the transfomer.
     */
    public void addTransformer(final String role, final String source, final Parameters param, final Parameters hintParam) {
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
        preparePipeline(environment);
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     */
    public boolean process(final Environment environment) throws ProcessingException {
        if (!this.prepared) {
            preparePipeline(environment);
        }
        // If this is an internal request, lastConsumer was reset!
        if (this.lastConsumer == null) {
            this.lastConsumer = this.serializer;
        }
        connectPipeline(environment);
        return processXMLPipeline(environment);
    }

    /**
     * Process the given <code>Environment</code>, but do not use the
     * serializer. Instead all SAX events are streamed to the XMLConsumer.
     */
    public boolean process(final Environment environment, final XMLConsumer consumer) throws ProcessingException {
        this.lastConsumer = consumer;
        connectPipeline(environment);
        return processXMLPipeline(environment);
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
        if (errorHandler != null) {
            throw new IllegalArgumentException("we don't use error handler any more.");
        }
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
    public void setGenerator(final String role, final String source, final Parameters param, final Parameters hintParam) {
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
    public void setReader(final String role, final String source, final Parameters param, final String mimeType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the serializer for this pipeline
     * 
     * @param mimeType
     *            Can be null
     */
    public void setSerializer(final String role, final String source, final Parameters param, final Parameters hintParam,
            final String mimeType) {
        // if (mimeType == null) {
        // throw new IllegalArgumentException("null mimeType");
        // }
        this.serializer = (Serializer) this.beanFactory.getBean(Serializer.ROLE + '/' + role);
        this.serializerSource = source;
        this.serializerParam = param;
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

    protected long getExpires() {
        return this.expires;
    }

    protected XMLConsumer getLastConsumer() {
        return this.lastConsumer;
    }

    protected int getOutputBufferSize() {
        return this.outputBufferSize;
    }

    protected Parameters getParameters() {
        return this.parameters;
    }

    protected Serializer getSerializer() {
        return this.serializer;
    }

    protected List<Transformer> getTransformers() {
        return this.transformers;
    }

    /**
     * Prepare the pipeline
     */
    protected void preparePipeline(final Environment environment) throws ProcessingException {
        setupPipeline(environment);
        this.prepared = true;
    }

    /**
     * Process the SAX event pipeline
     */
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        try {
            if (this.lastConsumer == null) {
                // internal processing
                this.generator.generate();
            } else {
                // set the output stream
                this.serializer.setOutputStream(environment.getOutputStream(this.outputBufferSize));
                // execute the pipeline:
                this.generator.generate();
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
        return true;
    }

    protected void setLastConsumer(final XMLConsumer consumer) {
        this.lastConsumer = consumer;
    }

    /**
     * Setup pipeline components.
     */
    @SuppressWarnings("rawtypes")
    protected void setupPipeline(final Environment environment) throws ProcessingException {
        try {
            // setup the generator
            Map model = environment.getObjectModel();
            this.generator.setup(this.sourceResolver, model, this.generatorSource, this.generatorParam);
            Iterator<Transformer> transformerItt = this.transformers.iterator();
            Iterator<String> transformerSourceItt = this.transformerSources.iterator();
            Iterator<Parameters> transformerParamItt = this.transformerParams.iterator();
            while (transformerItt.hasNext()) {
                Transformer trans = transformerItt.next();
                trans.setup(this.sourceResolver, model, transformerSourceItt.next(), transformerParamItt.next());
            }
            if (this.serializer instanceof SitemapModelComponent) {
                ((SitemapModelComponent) this.serializer).setup(this.sourceResolver, model, this.serializerSource,
                        this.serializerParam);
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    /**
     * Parse the expires parameter
     */
    private long parseExpires(final String expire) {
        StringTokenizer tokens = new StringTokenizer(expire);
        // get <base>
        String current = tokens.nextToken();
        if (current.equals("modification")) {
            current = "now";
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
                return -1;
            }
            // now get <modifier>
            try {
                current = tokens.nextToken();
            } catch (NoSuchElementException nsee) {
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
                return -1;
            }
            expires += number * modifier;
        }
        return expires;
    }
}
