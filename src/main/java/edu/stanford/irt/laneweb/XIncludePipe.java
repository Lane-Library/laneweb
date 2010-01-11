package edu.stanford.irt.laneweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.CascadingException;
import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.components.source.util.SourceUtil;
import org.apache.cocoon.components.xpointer.XPointer;
import org.apache.cocoon.components.xpointer.XPointerContext;
import org.apache.cocoon.components.xpointer.parser.ParseException;
import org.apache.cocoon.components.xpointer.parser.XPointerFrameworkParser;
import org.apache.cocoon.util.NetUtils;
import org.apache.cocoon.xml.AbstractXMLPipe;
import org.apache.cocoon.xml.IncludeXMLConsumer;
import org.apache.cocoon.xml.XMLBaseSupport;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * XMLPipe that processes XInclude elements. To perform XInclude processing on included content, this class is
 * instantiated recursively.
 */
class XIncludePipe extends AbstractXMLPipe {

    /**
     * The nesting level of xi:fallback elements that have been encountered.
     */
    private int fallbackElementLevel;

    /**
     * In case {@link #useFallbackLevel} > 0, then this should contain the exception that caused fallback to be needed.
     * In the case of nested include elements it will contain only the deepest exception.
     */
    private Exception fallBackException;

    /**
     * Value of the href attribute of the xi:include element that caused the creation of the this XIncludePipe. Used to
     * detect loop inclusions.
     */
    private String href;

    /**
     * 
     */
    private final LanewebXIncludeTransformer lanewebXIncludeTransformer;

    /**
     * Locator of the current stream, stored here so that it can be restored after another document send its content to
     * the consumer.
     */
    private Locator locator;

    /**
     * Keep a map of namespaces prefix in the source document to pass it to the XPointerContext for correct namespace
     * identification.
     */
    private Map<String, String> namespaces = new HashMap<String, String>();

    private XIncludePipe parent;

    /** The nesting level of fallback that should be used */
    private int useFallbackLevel = 0;

    /** The nesting level of xi:include elements that have been encountered. */
    private int xIncludeElementLevel = 0;

    /** Helper class to keep track of xml:base attributes */
    private XMLBaseSupport xmlBaseSupport;

    /**
     * Value of the xpointer attribute of the xi:include element that caused the creation of this XIncludePipe. Used to
     * detect loop inclusions.
     */
    private String xpointer;

    /**
     * @param lanewebXIncludeTransformer
     */
    XIncludePipe(final LanewebXIncludeTransformer lanewebXIncludeTransformer) {
        this.lanewebXIncludeTransformer = lanewebXIncludeTransformer;
    }

    @Override
    public void characters(final char chars[], final int start, final int len) throws SAXException {
        if (isEvaluatingContent()) {
            super.characters(chars, start, len);
        }
    }

    @Override
    public void comment(final char chars[], final int start, final int len) throws SAXException {
        if (isEvaluatingContent()) {
            super.comment(chars, start, len);
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        if (isEvaluatingContent()) {
            super.endCDATA();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // We won't be getting any more sources so mark the
        // MultiSourceValidity as finished.
        this.lanewebXIncludeTransformer.validity.close();
        super.endDocument();
    }

    @Override
    public void endElement(final String uri, final String name, final String raw) throws SAXException {
        // Track xml:base context:
        this.xmlBaseSupport.endElement(uri, name, raw);
        // Handle elements in xinclude namespace:
        if (LanewebXIncludeTransformer.XINCLUDE_NAMESPACE_URI.equals(uri)) {
            // Handle xi:include:
            if (LanewebXIncludeTransformer.XINCLUDE_INCLUDE_ELEMENT.equals(name)) {
                this.xIncludeElementLevel--;
                if (this.useFallbackLevel > this.xIncludeElementLevel) {
                    this.useFallbackLevel = this.xIncludeElementLevel;
                }
            } else if (LanewebXIncludeTransformer.XINCLUDE_FALLBACK_ELEMENT.equals(name)) {
                // Handle xi:fallback:
                this.fallbackElementLevel--;
            }
        } else if (isEvaluatingContent()) {
            // Copy other elements through when appropriate:
            super.endElement(uri, name, raw);
        }
    }

    @Override
    public void endEntity(final String name) throws SAXException {
        if (isEvaluatingContent()) {
            super.endEntity(name);
        }
    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        if (isEvaluatingContent()) {
            super.endPrefixMapping(prefix);
            this.namespaces.remove(prefix);
        }
    }

    public String getHref() {
        return this.href;
    }

    public XIncludePipe getParent() {
        return this.parent;
    }

    public String getXpointer() {
        return this.xpointer;
    }

    @Override
    public void ignorableWhitespace(final char chars[], final int start, final int len) throws SAXException {
        if (isEvaluatingContent()) {
            super.ignorableWhitespace(chars, start, len);
        }
    }

    public void init(final String uri, final String xpointer) {
        this.href = uri;
        this.xpointer = xpointer;
        this.xmlBaseSupport = new XMLBaseSupport(this.lanewebXIncludeTransformer.resolver, getLogger());
    }

    public boolean isLoopInclusion(final String uri, final String xpointer) {
        String thePointer = xpointer == null ? "" : xpointer;
        if (uri.equals(this.href) && thePointer.equals(this.xpointer == null ? "" : this.xpointer)) {
            return true;
        }
        XIncludePipe parent = getParent();
        while (parent != null) {
            if (uri.equals(parent.getHref())
                    && thePointer.equals(parent.getXpointer() == null ? "" : parent.getXpointer())) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        if (isEvaluatingContent()) {
            super.processingInstruction(target, data);
        }
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        try {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("setDocumentLocator called " + locator.getSystemId());
            }
            // When using SAXON to serialize a DOM tree to SAX, a locator is
            // passed with a "null" system id
            if (locator.getSystemId() != null) {
                Source source = this.lanewebXIncludeTransformer.resolver.resolveURI(locator.getSystemId());
                try {
                    this.xmlBaseSupport.setDocumentLocation(source.getURI());
                    // only for the "root" XIncludePipe, we'll have to set
                    // the href here, in the other cases
                    // the href is taken from the xi:include href attribute
                    if (this.href == null) {
                        this.href = source.getURI();
                    }
                } finally {
                    this.lanewebXIncludeTransformer.resolver.release(source);
                }
            }
        } catch (IOException e) {
            throw new CascadingRuntimeException(
                    "Error in XIncludeTransformer while trying to resolve base URL for document", e);
        } catch (SAXException e) {
            throw new CascadingRuntimeException(
                    "Error in XIncludeTransformer while trying to resolve base URL for document", e);
        }
        this.locator = locator;
        super.setDocumentLocator(locator);
    }

    public void setParent(final XIncludePipe parent) {
        this.parent = parent;
    }

    @Override
    public void skippedEntity(final String name) throws SAXException {
        if (isEvaluatingContent()) {
            super.skippedEntity(name);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        if (isEvaluatingContent()) {
            super.startCDATA();
        }
    }

    @Override
    public void startElement(final String uri, final String name, final String raw, final Attributes attr)
            throws SAXException {
        // Track xml:base context:
        this.xmlBaseSupport.startElement(uri, name, raw, attr);
        // Handle elements in xinclude namespace:
        if (LanewebXIncludeTransformer.XINCLUDE_NAMESPACE_URI.equals(uri)) {
            // Handle xi:include:
            if (LanewebXIncludeTransformer.XINCLUDE_INCLUDE_ELEMENT.equals(name)) {
                // Process the include, unless in an ignored fallback:
                if (isEvaluatingContent()) {
                    String href = attr.getValue("", LanewebXIncludeTransformer.XINCLUDE_INCLUDE_ELEMENT_HREF_ATTRIBUTE);
                    String parse =
                            attr.getValue("", LanewebXIncludeTransformer.XINCLUDE_INCLUDE_ELEMENT_PARSE_ATTRIBUTE);
                    String xpointer =
                            attr.getValue("", LanewebXIncludeTransformer.XINCLUDE_INCLUDE_ELEMENT_XPOINTER_ATTRIBUTE);
                    try {
                        processXIncludeElement(href, parse, xpointer);
                    } catch (ProcessingException e) {
                        getLogger().debug("Rethrowing exception", e);
                        throw new SAXException(e);
                    } catch (IOException e) {
                        getLogger().debug("Rethrowing exception", e);
                        throw new SAXException(e);
                    }
                }
                this.xIncludeElementLevel++;
            } else if (LanewebXIncludeTransformer.XINCLUDE_FALLBACK_ELEMENT.equals(name)) {
                // Handle xi:fallback
                this.fallbackElementLevel++;
            } else {
                // Unknown element:
                throw new SAXException("Unknown XInclude element " + raw + " at " + getLocation());
            }
        } else if (isEvaluatingContent()) {
            // Copy other elements through when appropriate:
            super.startElement(uri, name, raw, attr);
        }
    }

    @Override
    public void startEntity(final String name) throws SAXException {
        if (isEvaluatingContent()) {
            super.startEntity(name);
        }
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if (isEvaluatingContent()) {
            super.startPrefixMapping(prefix, uri);
            this.namespaces.put(prefix, uri);
        }
    }

    private String getLocation() {
        if (this.locator == null) {
            return "unknown location";
        } else {
            return this.locator.getSystemId() + ":" + this.locator.getColumnNumber() + ":"
                    + this.locator.getLineNumber();
        }
    }

    /**
     * Determine whether the pipe is currently in a state where contents should be evaluated, i.e. xi:include elements
     * should be resolved and elements in other namespaces should be copied through. Will return false for fallback
     * contents within a successful xi:include, and true for contents outside any xi:include or within an xi:fallback
     * for an unsuccessful xi:include.
     */
    private boolean isEvaluatingContent() {
        return (this.xIncludeElementLevel == 0)
                || ((this.fallbackElementLevel > 0) && (this.fallbackElementLevel == this.useFallbackLevel));
    }

    private void parseText(final String xpointer, final Source url) throws SAXException, IOException {
        getLogger().debug("Parse type is text");
        if (xpointer != null) {
            throw new SAXException("xpointer attribute must not be present when parse='text': " + getLocation());
        }
        InputStream is = null;
        InputStreamReader isr = null;
        Reader reader = null;
        try {
            is = url.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            int read;
            char[] ary = new char[1024 * 4];
            while ((read = reader.read(ary)) != -1) {
                super.characters(ary, 0, read);
            }
        } catch (SourceNotFoundException e) {
            this.useFallbackLevel++;
            this.fallBackException = new CascadingException("Resource not found: " + url.getURI());
            getLogger().error("xIncluded resource not found: " + url.getURI(), e);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    private void parseXML(final String xpointer, final Source url) throws ProcessingException {
        getLogger().debug("Parse type is XML");
        // Check loop inclusion
        if (isLoopInclusion(url.getURI(), xpointer)) {
            throw new ProcessingException("Detected loop inclusion of href=" + url.getURI() + ", xpointer=" + xpointer);
        }
        XIncludePipe subPipe = new XIncludePipe(this.lanewebXIncludeTransformer);
        subPipe.init(url.getURI(), xpointer);
        subPipe.setConsumer(this.xmlConsumer);
        subPipe.setParent(this);
        try {
            if ((xpointer != null) && (xpointer.length() > 0)) {
                XPointer xptr;
                xptr = XPointerFrameworkParser.parse(NetUtils.decodePath(xpointer));
                XPointerContext context =
                        new XPointerContext(xpointer, url, subPipe, this.lanewebXIncludeTransformer.manager);
                for (String prefix : this.namespaces.keySet()) {
                    context.addPrefix(prefix, this.namespaces.get(prefix));
                }
                xptr.process(context);
            } else {
                this.lanewebXIncludeTransformer.parser.parse(new InputSource(url.getInputStream()),
                        new IncludeXMLConsumer(subPipe));
            }
            // restore locator on the consumer
            if (this.locator != null) {
                this.xmlConsumer.setDocumentLocator(this.locator);
            }
        } catch (ResourceNotFoundException e) {
            this.useFallbackLevel++;
            this.fallBackException = new CascadingException("Resource not found: " + url.getURI());
            getLogger().error("xIncluded resource not found: " + url.getURI(), e);
        } catch (ParseException e) {
            // this exception is thrown in case of an invalid
            // xpointer expression
            this.useFallbackLevel++;
            this.fallBackException = new CascadingException("Error parsing xPointer expression", e);
            this.fallBackException.fillInStackTrace();
            getLogger().error("Error parsing XPointer expression, will try to use fallback.", e);
        } catch (SAXException e) {
            this.useFallbackLevel++;
            this.fallBackException = e;
            getLogger().error("Error processing an xInclude, will try to use fallback.", e);
        } catch (MalformedURLException e) {
            this.useFallbackLevel++;
            this.fallBackException = e;
            getLogger().error("Error processing an xInclude, will try to use fallback.", e);
        } catch (IOException e) {
            this.useFallbackLevel++;
            this.fallBackException = e;
            getLogger().error("Error processing an xInclude, will try to use fallback.", e);
        }
    }

    protected void processXIncludeElement(final String href, final String parse, final String xpointer)
            throws SAXException, ProcessingException, IOException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(
                    "Processing XInclude element: href=" + href + ", parse=" + parse + ", xpointer=" + xpointer);
        }
        // Default for @parse is "xml"
        String p = parse == null ? "xml" : parse;
        Source url = null;
        String h = href;
        try {
            // An empty or absent href is a reference to the current
            // document -- this can be different than the current base
            if ((href == null) || (href.length() == 0)) {
                if (this.href == null) {
                    throw new SAXException(
                            "XIncludeTransformer: encountered empty href (= href pointing to the current document) but the location of the current document is unknown.");
                }
                h = this.href;
            }
            url = this.xmlBaseSupport.makeAbsolute(h);
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("URL: " + url.getURI() + "\nXPointer: " + xpointer);
            }
            // add the source to the SourceValidity
            this.lanewebXIncludeTransformer.validity.addSource(url);
            if ("text".equals(p)) {
                parseText(xpointer, url);
            } else if ("xml".equals(p)) {
                parseXML(xpointer, url);
            } else {
                throw new SAXException("Found 'parse' attribute with unknown value " + p + " at " + getLocation());
            }
        } catch (SourceException se) {
            throw SourceUtil.handle(se);
        } finally {
            if (url != null) {
                this.lanewebXIncludeTransformer.resolver.release(url);
            }
        }
    }
}