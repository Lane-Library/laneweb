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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.wrapper.RequestParameters;
import org.apache.cocoon.xml.ContentHandlerWrapper;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import edu.stanford.irt.laneweb.cocoon.LanewebEnvironment;
import edu.stanford.irt.laneweb.model.Model;

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
    private Environment environment;

    /** The pipeline description */
    private Processor.InternalPipelineDescription pipelineDescription;

    /** The used protocol */
    private String protocol;

    /** The system id used for caching */
    private String systemIdForCaching;

    /** The internal event pipeline validities */
    private SitemapSourceValidity validity;
    
    private ByteArrayOutputStream outputStream;

    /**
     * Construct a new object
     * 
     * @throws Exception
     */
    //TODO: a lot of this should probably be in the SourceFactory
    @SuppressWarnings("unchecked")
    public LanewebSitemapSource(final String uri, final Environment environment, final Processor processor) {
        this.validity = new SitemapSourceValidity();
        int colon = uri.indexOf(':');
        this.protocol = uri.substring(0, colon);
        Map<String, Object> newObjectModel = new HashMap<String, Object>(environment.getObjectModel());
        int startOfPath = uri.indexOf(":/") + 2;
        String sitemapURI = uri.substring(startOfPath);
        int qMark = sitemapURI.indexOf('?');
        if (qMark > -1) {
            sitemapURI = sitemapURI.substring(qMark);
            // add uri parameters to newObjectModel
            RequestParameters params = new RequestParameters(sitemapURI.substring(qMark + 1));
            for (Enumeration<String> names = params.getParameterNames(); names.hasMoreElements();) {
                String name = names.nextElement();
                String[] value = params.getParameterValues(name);
                if (value.length == 1) {
                    newObjectModel.put(name, value[0]);
                } else {
                    newObjectModel.put(name, value);
                }
            }
        }
        newObjectModel.put(Model.SITEMAP_URI, sitemapURI);
        this.outputStream = new ByteArrayOutputStream();
        this.environment = new LanewebEnvironment(newObjectModel, this.outputStream, false);
        try {
            this.pipelineDescription = processor.buildPipeline(this.environment);
            this.pipelineDescription.processingPipeline.prepareInternal(this.environment);
        } catch (Exception e) {
            throw new LanewebSourceException(e);
        }
        this.validity.set(this.pipelineDescription.processingPipeline.getValidityForEventPipeline());
        String eventPipelineKey = this.pipelineDescription.processingPipeline.getKeyForEventPipeline();
        if (eventPipelineKey != null) {
            StringBuilder buffer = new StringBuilder(uri);
            if (uri.indexOf('?') == -1) {
                buffer.append('?');
            } else {
                buffer.append('&');
            }
            buffer.append("pipelinehash=");
            buffer.append(eventPipelineKey);
            this.systemIdForCaching = buffer.toString();
        } else {
            this.systemIdForCaching = uri;
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
        try {
            this.pipelineDescription.processingPipeline.process(this.environment);
        } catch (ProcessingException e) {
            throw new LanewebSourceException(e);
        }
        return new ByteArrayInputStream(this.outputStream.toByteArray());
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
        throw new UnsupportedOperationException();
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
        try {
            this.pipelineDescription.processingPipeline.process(this.environment, consumer);
        } catch (ProcessingException e) {
            throw new LanewebSourceException(e);
        }
    }
}
