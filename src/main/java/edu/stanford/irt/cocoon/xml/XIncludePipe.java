package edu.stanford.irt.cocoon.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.components.source.impl.MultiSourceValidity;
import org.apache.cocoon.components.xpointer.XPointer;
import org.apache.cocoon.components.xpointer.XPointerContext;
import org.apache.cocoon.components.xpointer.parser.ParseException;
import org.apache.cocoon.components.xpointer.parser.XPointerFrameworkParser;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AbstractXMLPipe;
import org.apache.cocoon.xml.IncludeXMLConsumer;
import org.apache.cocoon.xml.XMLBaseSupport;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * XMLPipe that processes XInclude elements. To perform XInclude processing on
 * included content, this class is instantiated recursively.
 */
public class XIncludePipe extends AbstractXMLPipe {

    private static final String FALLBACK = "fallback";

    private static final String HREF = "href";

    private static final String INCLUDE = "include";

    private static final String NAMESPACE = "http://www.w3.org/2001/XInclude";

    private static final String PARSE = "parse";

    private static final String XPOINTER = "xpointer";

    private XIncludeExceptionListener exceptionListener;

    /**
     * The nesting level of xi:fallback elements that have been encountered.
     */
    private int fallbackElementLevel;

    /**
     * Value of the href attribute of the xi:include element that caused the
     * creation of the this XIncludePipe. Used to detect loop inclusions.
     */
    private String href;

    /**
     * Locator of the current stream, stored here so that it can be restored
     * after another document send its content to the consumer.
     */
    private Locator locator;

    /**
     * Keep a map of namespaces prefix in the source document to pass it to the
     * XPointerContext for correct namespace identification.
     */
    private Map<String, String> namespaces = new HashMap<String, String>();

    private XIncludePipe parent;

    private SAXParser saxParser;

    private ServiceManager serviceManager;

    private SourceResolver sourceResolver;

    /** The nesting level of fallback that should be used */
    private int useFallbackLevel = 0;

    private MultiSourceValidity validity;

    /** The nesting level of xi:include elements that have been encountered. */
    private int xIncludeElementLevel = 0;

    /** Helper class to keep track of xml:base attributes */
    private XMLBaseSupport xmlBaseSupport;

    /**
     * Value of the xpointer attribute of the xi:include element that caused the
     * creation of this XIncludePipe. Used to detect loop inclusions.
     */
    private String xpointer;

    public XIncludePipe(final SourceResolver sourceResolver, final MultiSourceValidity validity,
            final ServiceManager serviceManager, final SAXParser saxParser, final XIncludeExceptionListener exceptionLister,
            final XMLBaseSupport xmlBaseSupport) {
        this(sourceResolver, validity, serviceManager, saxParser, exceptionLister, xmlBaseSupport, null);
    }

    /**
     * @param validity
     * @param parser
     * @param lanewebXIncludeTransformer
     */
    XIncludePipe(final SourceResolver sourceResolver, final MultiSourceValidity validity, final ServiceManager serviceManager,
            final SAXParser saxParser, final XIncludeExceptionListener exceptionLister, final XMLBaseSupport xmlBaseSupport,
            final XIncludePipe parent) {
        this.sourceResolver = sourceResolver;
        this.validity = validity;
        this.serviceManager = serviceManager;
        this.saxParser = saxParser;
        this.exceptionListener = exceptionLister;
        this.xmlBaseSupport = xmlBaseSupport;
        this.parent = parent;
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
        this.validity.close();
        super.endDocument();
    }

    @Override
    public void endElement(final String uri, final String name, final String raw) throws SAXException {
        // Track xml:base context:
        this.xmlBaseSupport.endElement(uri, name, raw);
        // Handle elements in xinclude namespace:
        if (NAMESPACE.equals(uri)) {
            // Handle xi:include:
            if (INCLUDE.equals(name)) {
                this.xIncludeElementLevel--;
                if (this.useFallbackLevel > this.xIncludeElementLevel) {
                    this.useFallbackLevel = this.xIncludeElementLevel;
                }
            } else if (FALLBACK.equals(name)) {
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

    @Override
    public void ignorableWhitespace(final char chars[], final int start, final int len) throws SAXException {
        if (isEvaluatingContent()) {
            super.ignorableWhitespace(chars, start, len);
        }
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
            // When using SAXON to serialize a DOM tree to SAX, a locator is
            // passed with a "null" system id
            String systemid = locator.getSystemId();
            if (systemid != null) {
                Source source = this.sourceResolver.resolveURI(systemid);
                String uri = source.getURI();
                this.xmlBaseSupport.setDocumentLocation(uri);
                // only for the "root" XIncludePipe, we'll have to set
                // the href here, in the other cases
                // the href is taken from the xi:include href attribute
                if (this.href == null) {
                    this.href = uri;
                }
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
        this.locator = locator;
        super.setDocumentLocator(locator);
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

    // private void setParent(final XIncludePipe parent) {
    // this.parent = parent;
    // }
    @Override
    public void startElement(final String uri, final String name, final String raw, final Attributes attr) throws SAXException {
        // Track xml:base context:
        this.xmlBaseSupport.startElement(uri, name, raw, attr);
        // Handle elements in xinclude namespace:
        if (NAMESPACE.equals(uri)) {
            // Handle xi:include:
            if (INCLUDE.equals(name)) {
                // Process the include, unless in an ignored fallback:
                if (isEvaluatingContent()) {
                    String hrefAttr = attr.getValue("", HREF);
                    String parseAttr = attr.getValue("", PARSE);
                    String xpointerAttr = attr.getValue("", XPOINTER);
                    try {
                        processXIncludeElement(hrefAttr, parseAttr, xpointerAttr);
                    } catch (Exception e) {
                        this.useFallbackLevel++;
                        this.exceptionListener.exception(e);
                    }
                }
                this.xIncludeElementLevel++;
            } else if (FALLBACK.equals(name)) {
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

    // TODO: have this make sense
    public void init(final String uri, final String xpointer) {
        this.href = uri;
        this.xpointer = xpointer;
    }

    private String getLocation() {
        if (this.locator == null) {
            return "unknown location";
        } else {
            return this.locator.getSystemId() + ":" + this.locator.getColumnNumber() + ":" + this.locator.getLineNumber();
        }
    }

    /**
     * Determine whether the pipe is currently in a state where contents should
     * be evaluated, i.e. xi:include elements should be resolved and elements in
     * other namespaces should be copied through. Will return false for fallback
     * contents within a successful xi:include, and true for contents outside
     * any xi:include or within an xi:fallback for an unsuccessful xi:include.
     */
    private boolean isEvaluatingContent() {
        return (this.xIncludeElementLevel == 0)
                || ((this.fallbackElementLevel > 0) && (this.fallbackElementLevel == this.useFallbackLevel));
//        boolean xincludeElementLevelIsZero = this.xIncludeElementLevel == 0;
//        boolean fallbackElementLevelGreaterThanZero = this.fallbackElementLevel > 0;
//        boolean fallbackElementLevelEqualsUseFallbackLevel = this.fallbackElementLevel == this.useFallbackLevel;
//        boolean fallbackElementLevelGreaterThanZeroAndFallbackElementLevelEqualsUseFallbackLevel = fallbackElementLevelGreaterThanZero && fallbackElementLevelEqualsUseFallbackLevel;
//        boolean isEvaluatingContent = xincludeElementLevelIsZero || fallbackElementLevelGreaterThanZeroAndFallbackElementLevelEqualsUseFallbackLevel;
//        return isEvaluatingContent;
    }

    private boolean isLoopInclusion(final String uri, final String xpointer) {
        String thePointer = xpointer == null ? "" : xpointer;
        if (uri.equals(this.href) && thePointer.equals(this.xpointer == null ? "" : this.xpointer)) {
            return true;
        }
        XIncludePipe parentPipe = this.parent;
        while (parentPipe != null) {
            if (uri.equals(parentPipe.href) && thePointer.equals(parentPipe.xpointer == null ? "" : parentPipe.xpointer)) {
                return true;
            }
            parentPipe = parentPipe.parent;
        }
        return false;
    }

    private void parseText(final Source source) throws IOException, SAXException {
        InputStream is = null;
        InputStreamReader isr = null;
        Reader reader = null;
        try {
            is = source.getInputStream();
            isr = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(isr);
            int read;
            char[] ary = new char[1024];
            while ((read = reader.read(ary)) != -1) {
                super.characters(ary, 0, read);
            }
        } finally {
            if (reader != null) {
                reader.close();
            } else if (isr != null) {
                isr.close();
            } else if (is != null) {
                is.close();
            }
        }
    }

    private void parseXML(final String xpointer, final Source source) throws ParseException, SAXException, IOException {
        // Check loop inclusion
        String uri = source.getURI();
        if (isLoopInclusion(uri, xpointer)) {
            throw new IllegalStateException("Detected loop inclusion of href=" + uri + ", xpointer=" + xpointer);
        }
        XIncludePipe subPipe = new XIncludePipe(this.sourceResolver, this.validity, this.serviceManager, this.saxParser,
                this.exceptionListener, this.xmlBaseSupport, this);
        subPipe.init(uri, xpointer);
        subPipe.setConsumer(this.xmlConsumer);
        if ((xpointer != null) && (xpointer.length() > 0)) {
            XPointer xptr = XPointerFrameworkParser.parse(URLDecoder.decode(xpointer, "UTF-8"));
            XPointerContext context = new XPointerContext(xpointer, source, subPipe, this.serviceManager);
            for (String prefix : this.namespaces.keySet()) {
                context.addPrefix(prefix, this.namespaces.get(prefix));
            }
            try {
                xptr.process(context);
            } catch (ResourceNotFoundException e) {
                throw new LanewebException(e);
            }
        } else if (source instanceof XMLizable) {
            ((XMLizable) source).toSAX(new IncludeXMLConsumer(subPipe));
        } else {
            InputSource inputSource = new InputSource();
            try {
                inputSource.setByteStream(source.getInputStream());
                inputSource.setSystemId(uri);
                this.saxParser.parse(inputSource, new IncludeXMLConsumer(subPipe));
            } finally {
                if (inputSource.getByteStream() != null) {
                    inputSource.getByteStream().close();
                }
            }
        }
        // restore locator on the consumer
        if (this.locator != null) {
            this.xmlConsumer.setDocumentLocator(this.locator);
        }
    }

    private void processXIncludeElement(final String href, final String parse, final String xpointer) throws IOException,
            SAXException, ParseException {
        // Default for @parse is "xml"
        String localParse = parse == null ? "xml" : parse;
        Source url = null;
        String localHref = href;
        // An empty or absent href is a reference to the current
        // document -- this can be different than the current base
        if ((href == null) || (href.length() == 0)) {
            if (this.href == null) {
                throw new IllegalArgumentException(
                        "XIncludeTransformer: encountered empty href (= href pointing to the current document) but the location of the current document is unknown.");
            }
            localHref = this.href;
        }
        url = this.xmlBaseSupport.makeAbsolute(localHref);
        // add the source to the SourceValidity
        this.validity.addSource(url);
        if ("text".equals(localParse)) {
            parseText(url);
        } else if ("xml".equals(localParse)) {
            parseXML(xpointer, url);
        }
    }
}