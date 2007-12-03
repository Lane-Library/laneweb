/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.stanford.irt.laneweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.avalon.framework.CascadingException;
import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.components.source.impl.MultiSourceValidity;
import org.apache.cocoon.components.xpointer.XPointer;
import org.apache.cocoon.components.xpointer.XPointerContext;
import org.apache.cocoon.components.xpointer.parser.ParseException;
import org.apache.cocoon.components.xpointer.parser.XPointerFrameworkParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.util.NetUtils;
import org.apache.cocoon.xml.AbstractXMLPipe;
import org.apache.cocoon.xml.IncludeXMLConsumer;
import org.apache.cocoon.xml.XMLBaseSupport;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * shamelessly copied from cocoon 2.2-M2, modified only startPrefixMapping()
 * --cy also altered to use fallback if ProcessingException or SAXException
 * 
 */
public class LanewebXIncludeTransformer extends AbstractTransformer implements Serviceable, CacheableProcessingComponent {

    protected SourceResolver resolver;

    protected ServiceManager manager;

    private XIncludePipe xIncludePipe;

    public static final String XMLBASE_ATTRIBUTE = "base";

    public static final String XINCLUDE_NAMESPACE_URI = "http://www.w3.org/2001/XInclude";

    public static final String XINCLUDE_INCLUDE_ELEMENT = "include";

    public static final String XINCLUDE_FALLBACK_ELEMENT = "fallback";

    public static final String XINCLUDE_INCLUDE_ELEMENT_HREF_ATTRIBUTE = "href";

    public static final String XINCLUDE_INCLUDE_ELEMENT_XPOINTER_ATTRIBUTE = "xpointer";

    public static final String XINCLUDE_INCLUDE_ELEMENT_PARSE_ATTRIBUTE = "parse";

    private static final String XINCLUDE_CACHE_KEY = "XInclude";

    /** The {@link SourceValidity} instance associated with this request. */
    protected MultiSourceValidity validity;

    public void setup(final SourceResolver resolver, final Map objectModel, final String source, final Parameters parameters)
            throws ProcessingException, SAXException, IOException {
        this.resolver = resolver;
        this.validity = new MultiSourceValidity(resolver, MultiSourceValidity.CHECK_ALWAYS);
        this.xIncludePipe = new XIncludePipe();
        this.xIncludePipe.enableLogging(getLogger());
        this.xIncludePipe.init(null, null);
        super.setContentHandler(this.xIncludePipe);
        super.setLexicalHandler(this.xIncludePipe);
    }

    @Override
    public void setConsumer(final XMLConsumer consumer) {
        this.xIncludePipe.setConsumer(consumer);
    }

    @Override
    public void setContentHandler(final ContentHandler handler) {
        this.xIncludePipe.setContentHandler(handler);
    }

    @Override
    public void setLexicalHandler(final LexicalHandler handler) {
        this.xIncludePipe.setLexicalHandler(handler);
    }

    public void service(final ServiceManager manager) {
        this.manager = manager;
    }

    /** Key to be used for caching */
    public Serializable getKey() {
        return XINCLUDE_CACHE_KEY;
    }

    /** Get the validity for this transform */
    public SourceValidity getValidity() {
        return this.validity;
    }

    @Override
    public void recycle() {
        // Reset all variables to initial state.
        this.resolver = null;
        this.validity = null;
        this.xIncludePipe = null;
        super.recycle();
    }

    /**
     * XMLPipe that processes XInclude elements. To perform XInclude processing
     * on included content, this class is instantiated recursively.
     */
    private class XIncludePipe extends AbstractXMLPipe {

        /** Helper class to keep track of xml:base attributes */
        private XMLBaseSupport xmlBaseSupport;

        /** The nesting level of xi:include elements that have been encountered. */
        private int xIncludeElementLevel = 0;

        /** The nesting level of fallback that should be used */
        private int useFallbackLevel = 0;

        /** The nesting level of xi:fallback elements that have been encountered. */
        private int fallbackElementLevel;

        /**
         * In case {@link #useFallbackLevel} > 0, then this should contain the
         * exception that caused fallback to be needed. In the case of nested
         * include elements it will contain only the deepest exception.
         */
        private Exception fallBackException;

        /**
         * Locator of the current stream, stored here so that it can be restored
         * after another document send its content to the consumer.
         */
        private Locator locator;

        /**
         * Value of the href attribute of the xi:include element that caused the
         * creation of the this XIncludePipe. Used to detect loop inclusions.
         */
        private String href;

        /**
         * Value of the xpointer attribute of the xi:include element that caused
         * the creation of this XIncludePipe. Used to detect loop inclusions.
         */
        private String xpointer;

        private XIncludePipe parent;

        public void init(final String uri, final String xpointer) {
            this.href = uri;
            this.xpointer = xpointer;
            this.xmlBaseSupport = new XMLBaseSupport(LanewebXIncludeTransformer.this.resolver, getLogger());
        }

        public void setParent(final XIncludePipe parent) {
            this.parent = parent;
        }

        public XIncludePipe getParent() {
            return this.parent;
        }

        public String getHref() {
            return this.href;
        }

        public String getXpointer() {
            return this.xpointer;
        }

        /**
         * Determine whether the pipe is currently in a state where contents
         * should be evaluated, i.e. xi:include elements should be resolved and
         * elements in other namespaces should be copied through. Will return
         * false for fallback contents within a successful xi:include, and true
         * for contents outside any xi:include or within an xi:fallback for an
         * unsuccessful xi:include.
         */
        private boolean isEvaluatingContent() {
            return this.xIncludeElementLevel == 0
                    || (this.fallbackElementLevel > 0 && this.fallbackElementLevel == this.useFallbackLevel);
        }

        @Override
        public void endDocument() throws SAXException {
            // We won't be getting any more sources so mark the
            // MultiSourceValidity as finished.
            LanewebXIncludeTransformer.this.validity.close();
            super.endDocument();
        }

        @Override
        public void startElement(final String uri, final String name, final String raw, final Attributes attr) throws SAXException {
            // Track xml:base context:
            this.xmlBaseSupport.startElement(uri, name, raw, attr);
            // Handle elements in xinclude namespace:
            if (XINCLUDE_NAMESPACE_URI.equals(uri)) {
                // Handle xi:include:
                if (XINCLUDE_INCLUDE_ELEMENT.equals(name)) {
                    // Process the include, unless in an ignored fallback:
                    if (isEvaluatingContent()) {
                        String href = attr.getValue("", XINCLUDE_INCLUDE_ELEMENT_HREF_ATTRIBUTE);
                        String parse = attr.getValue("", XINCLUDE_INCLUDE_ELEMENT_PARSE_ATTRIBUTE);
                        String xpointer = attr.getValue("", XINCLUDE_INCLUDE_ELEMENT_XPOINTER_ATTRIBUTE);

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
                } else if (XINCLUDE_FALLBACK_ELEMENT.equals(name)) {
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
        public void endElement(final String uri, final String name, final String raw) throws SAXException {
            // Track xml:base context:
            this.xmlBaseSupport.endElement(uri, name, raw);

            // Handle elements in xinclude namespace:
            if (XINCLUDE_NAMESPACE_URI.equals(uri)) {
                // Handle xi:include:
                if (XINCLUDE_INCLUDE_ELEMENT.equals(name)) {
                    this.xIncludeElementLevel--;
                    if (this.useFallbackLevel > this.xIncludeElementLevel) {
                        this.useFallbackLevel = this.xIncludeElementLevel;
                    }
                } else if (XINCLUDE_FALLBACK_ELEMENT.equals(name)) {
                    // Handle xi:fallback:
                    this.fallbackElementLevel--;
                }
            } else if (isEvaluatingContent()) {
                // Copy other elements through when appropriate:
                super.endElement(uri, name, raw);
            }
        }

        @Override
        public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
            // don't propagate xinclude namespace declarations
            if (XINCLUDE_NAMESPACE_URI.equals(uri)) {
                return;
            }
            if (isEvaluatingContent()) {
                super.startPrefixMapping(prefix, uri);
            }
        }

        @Override
        public void endPrefixMapping(final String prefix) throws SAXException {
            if (isEvaluatingContent()) {
                super.endPrefixMapping(prefix);
            }
        }

        @Override
        public void characters(final char c[], final int start, final int len) throws SAXException {
            if (isEvaluatingContent()) {
                super.characters(c, start, len);
            }
        }

        @Override
        public void ignorableWhitespace(final char c[], final int start, final int len) throws SAXException {
            if (isEvaluatingContent()) {
                super.ignorableWhitespace(c, start, len);
            }
        }

        @Override
        public void processingInstruction(final String target, final String data) throws SAXException {
            if (isEvaluatingContent()) {
                super.processingInstruction(target, data);
            }
        }

        @Override
        public void skippedEntity(final String name) throws SAXException {
            if (isEvaluatingContent()) {
                super.skippedEntity(name);
            }
        }

        @Override
        public void startEntity(final String name) throws SAXException {
            if (isEvaluatingContent()) {
                super.startEntity(name);
            }
        }

        @Override
        public void endEntity(final String name) throws SAXException {
            if (isEvaluatingContent()) {
                super.endEntity(name);
            }
        }

        @Override
        public void startCDATA() throws SAXException {
            if (isEvaluatingContent()) {
                super.startCDATA();
            }
        }

        @Override
        public void endCDATA() throws SAXException {
            if (isEvaluatingContent()) {
                super.endCDATA();
            }
        }

        @Override
        public void comment(final char ch[], final int start, final int len) throws SAXException {
            if (isEvaluatingContent()) {
                super.comment(ch, start, len);
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
                    Source source = LanewebXIncludeTransformer.this.resolver.resolveURI(locator.getSystemId());
                    try {
                        this.xmlBaseSupport.setDocumentLocation(source.getURI());
                        // only for the "root" XIncludePipe, we'll have to set
                        // the href here, in the other cases
                        // the href is taken from the xi:include href attribute
                        if (this.href == null) {
                            this.href = source.getURI();
                        }
                    } finally {
                        LanewebXIncludeTransformer.this.resolver.release(source);
                    }
                }
            } catch (Exception e) {
                throw new CascadingRuntimeException("Error in XIncludeTransformer while trying to resolve base URL for document", e);
            }
            this.locator = locator;
            super.setDocumentLocator(locator);
        }

        protected void processXIncludeElement(String href, String parse, String xpointer) throws SAXException, ProcessingException,
                IOException {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Processing XInclude element: href=" + href + ", parse=" + parse + ", xpointer=" + xpointer);
            }

            // Default for @parse is "xml"
            if (parse == null) {
                parse = "xml";
            }
            Source url = null;

            try {
                int fragmentIdentifierPos = href.indexOf('#');
                if (fragmentIdentifierPos != -1) {
                    getLogger().warn(
                                     "Fragment identifer found in 'href' attribute: " + href
                                             + "\nFragment identifiers are forbidden by the XInclude specification. "
                                             + "They are still handled by XIncludeTransformer for backward "
                                             + "compatibility, but their use is deprecated and will be prohibited "
                                             + "in a future release.  Use the 'xpointer' attribute instead.");
                    if (xpointer == null) {
                        xpointer = href.substring(fragmentIdentifierPos + 1);
                    }
                    href = href.substring(0, fragmentIdentifierPos);
                }

                // An empty or absent href is a reference to the current
                // document -- this can be different than the current base
                if (href == null || href.length() == 0) {
                    if (this.href == null) {
                        throw new SAXException(
                                               "XIncludeTransformer: encountered empty href (= href pointing to the current document) but the location of the current document is unknown.");
                    }
                    // The following can be simplified once fragment identifiers
                    // are prohibited
                    int fragmentIdentifierPos2 = this.href.indexOf('#');
                    if (fragmentIdentifierPos2 != -1) {
                        href = this.href.substring(0, fragmentIdentifierPos2);
                    } else {
                        href = this.href;
                    }
                }

                url = this.xmlBaseSupport.makeAbsolute(href);
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("URL: " + url.getURI() + "\nXPointer: " + xpointer);
                }

                // add the source to the SourceValidity
                LanewebXIncludeTransformer.this.validity.addSource(url);

                if (parse.equals("text")) {
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
                        char ary[] = new char[1024 * 4];
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
                } else if (parse.equals("xml")) {
                    getLogger().debug("Parse type is XML");

                    // Check loop inclusion
                    if (isLoopInclusion(url.getURI(), xpointer)) {
                        throw new ProcessingException("Detected loop inclusion of href=" + url.getURI() + ", xpointer=" + xpointer);
                    }

                    XIncludePipe subPipe = new XIncludePipe();
                    subPipe.enableLogging(getLogger());
                    subPipe.init(url.getURI(), xpointer);
                    subPipe.setConsumer(this.xmlConsumer);
                    subPipe.setParent(this);

                    try {
                        if (xpointer != null && xpointer.length() > 0) {
                            XPointer xptr;
                            xptr = XPointerFrameworkParser.parse(NetUtils.decodePath(xpointer));
                            XPointerContext context =
                                    new XPointerContext(xpointer, url, subPipe, getLogger(),
                                                        LanewebXIncludeTransformer.this.manager);
                            xptr.process(context);
                        } else {
                            SourceUtil.toSAX(url, new IncludeXMLConsumer(subPipe));
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
                    } catch (ProcessingException e) {
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
                } else {
                    throw new SAXException("Found 'parse' attribute with unknown value " + parse + " at " + getLocation());
                }
            } catch (SourceException se) {
                throw SourceUtil.handle(se);
            } finally {
                if (url != null) {
                    LanewebXIncludeTransformer.this.resolver.release(url);
                }
            }
        }

        public boolean isLoopInclusion(final String uri, String xpointer) {
            if (xpointer == null) {
                xpointer = "";
            }

            if (uri.equals(this.href) && xpointer.equals(this.xpointer == null ? "" : this.xpointer)) {
                return true;
            }

            XIncludePipe parent = getParent();
            while (parent != null) {
                if (uri.equals(parent.getHref()) && xpointer.equals(parent.getXpointer() == null ? "" : parent.getXpointer())) {
                    return true;
                }
                parent = parent.getParent();
            }
            return false;
        }

        private String getLocation() {
            if (this.locator == null) {
                return "unknown location";
            }
            return this.locator.getSystemId() + ":" + this.locator.getColumnNumber() + ":" + this.locator.getLineNumber();
        }
    }
}
