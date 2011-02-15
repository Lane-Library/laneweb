package edu.stanford.irt.laneweb.cocoon.source;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.Processor;
import org.apache.cocoon.components.source.impl.SitemapSourceInfo;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.wrapper.EnvironmentWrapper;
import org.apache.cocoon.xml.ContentHandlerWrapper;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import edu.stanford.irt.laneweb.cocoon.pipeline.LanewebEnvironment;

/**
 * Implementation of a {@link Source} that gets its content by invoking a
 * pipeline.
 */
public class LanewebSitemapSource implements Source, XMLizable {

    /**
     * A simple SourceValidity protecting callers from resets.
     */
    public static class SitemapSourceValidity implements SourceValidity {

        private static final long serialVersionUID = 1L;

        private SourceValidity validity;

        protected SitemapSourceValidity() {
            super();
        }

        public SourceValidity getNestedValidity() {
            return this.validity;
        }

        public int isValid() {
            return (this.validity != null ? this.validity.isValid() : SourceValidity.INVALID);
        }

        public int isValid(final SourceValidity validity) {
            if (validity instanceof SitemapSourceValidity) {
                return (this.validity != null ? this.validity.isValid(((SitemapSourceValidity) validity).getNestedValidity())
                        : SourceValidity.INVALID);
            }
            return SourceValidity.INVALID;
        }

        protected void set(final SourceValidity validity) {
            this.validity = validity;
        }
    }

    /** The environment */
    private EnvironmentWrapper environment;

    private String mimeType;

    /** The pipeline description */
    private Processor.InternalPipelineDescription pipelineDescription;

    /** The processor */
    private Processor processor;

    /** The used protocol */
    private String protocol;

    /** The system id */
    private String systemId;

    /** The system id used for caching */
    private String systemIdForCaching;

    /** The internal event pipeline validities */
    private SitemapSourceValidity validity;

    /**
     * Construct a new object
     */
    public LanewebSitemapSource(final String uri, final Environment environment, final Processor processor) throws MalformedURLException {
        this.systemId = uri;
        this.processor = processor;
        // SitemapSourceInfo info = SitemapSourceInfo.parseURI(this.environment,
        // uri);
        // this.protocol = info.protocol;
        // create a new validity holder
        this.validity = new SitemapSourceValidity();
        // initialize
        // create environment...
        SitemapSourceInfo info = new SitemapSourceInfo();
        info.uri = uri.substring(uri.indexOf(":/") + 2);
        this.environment = new EnvironmentWrapper(environment, info);
        // The environment is a facade whose delegate can be changed in case of
        // internal redirects
        // this.environment = new MutableEnvironmentFacade(wrapper);
        try {
            this.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns true always.
     * 
     * @see org.apache.excalibur.source.Source#exists()
     */
    public boolean exists() {
        return true;
    }

    /**
     * Get the content length of the source or -1 if it is not possible to
     * determine the length.
     */
    public long getContentLength() {
        return -1;
    }

    /**
     * Return an <code>InputStream</code> object to read from the source.
     */
    public InputStream getInputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.environment.setOutputStream(os);
        try {
            this.pipelineDescription.processingPipeline.process(this.environment);
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * Get the last modification date.
     * 
     * @return The last modification in milliseconds since January 1, 1970 GMT
     *         or 0 if it is unknown
     */
    public long getLastModified() {
        return 0;
    }

    /**
     * The mime-type of the content described by this object. If the source is
     * not able to determine the mime-type by itself this can be null.
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Return the protocol identifier.
     */
    public String getScheme() {
        return this.protocol;
    }

    /**
     * Returns the unique identifer for this source
     */
    public String getURI() {
        return this.systemIdForCaching;
    }

    /**
     * Get the validity object. This wraps validity of the enclosed event
     * pipeline. If pipeline is not cacheable, <code>null</code> is returned.
     */
    public SourceValidity getValidity() {
        return this.validity.getNestedValidity() == null ? null : this.validity;
    }

    /**
     * Refresh this object and update the last modified date and content length.
     */
    public void refresh() {
        throw new UnsupportedOperationException();
    }

    /**
     * Stream content to the content handler
     */
    public void toSAX(final ContentHandler contentHandler) throws SAXException {
        XMLConsumer consumer;
        if (contentHandler instanceof XMLConsumer) {
            consumer = (XMLConsumer) contentHandler;
        } else if (contentHandler instanceof LexicalHandler) {
            consumer = new ContentHandlerWrapper(contentHandler, (LexicalHandler) contentHandler);
        } else {
            consumer = new ContentHandlerWrapper(contentHandler);
        }
        // We have to add an environment changer
        // for clean environment stack handling.
        try {
            this.pipelineDescription.processingPipeline.process(this.environment, consumer);
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize
     * 
     * @throws Exception
     */
    protected void init() throws Exception {
        this.systemIdForCaching = this.systemId;
        this.environment.startingProcessing();
        this.pipelineDescription = this.processor.buildPipeline(this.environment);
        this.environment.setURI(this.pipelineDescription.prefix, this.pipelineDescription.uri);
            this.pipelineDescription.processingPipeline.prepareInternal(this.environment);
            this.validity.set(this.pipelineDescription.processingPipeline.getValidityForEventPipeline());
            String eventPipelineKey = this.pipelineDescription.processingPipeline.getKeyForEventPipeline();
            this.mimeType = this.environment.getContentType();
            if (eventPipelineKey != null) {
                StringBuffer buffer = new StringBuffer(this.systemId);
                if (this.systemId.indexOf('?') == -1) {
                    buffer.append('?');
                } else {
                    buffer.append('&');
                }
                buffer.append("pipelinehash=");
                buffer.append(eventPipelineKey);
                this.systemIdForCaching = buffer.toString();
            } else {
                this.systemIdForCaching = this.systemId;
            }
    }
}
