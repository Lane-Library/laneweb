package edu.stanford.irt.cocoon.sitemap.source;

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

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.xml.ContentHandlerWrapper;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import edu.stanford.irt.cocoon.source.SourceException;

/**
 * Implementation of a {@link Source} that gets its content by invoking a
 * pipeline.
 */
public class SitemapSource implements Source, XMLizable {

    /** The environment */
    private Environment environment;

    private ByteArrayOutputStream outputStream;

    private ProcessingPipeline pipeline;

    /** The used protocol */
    private String protocol;

    /** The system id used for caching */
    private String systemIdForCaching;

    private String uri;

    /** The internal event pipeline validities */
    private SourceValidity validity;

    public SitemapSource(final String uri, final Environment environment, final ProcessingPipeline pipeline,
            final ByteArrayOutputStream outputStream) {
        this.uri = uri;
        this.environment = environment;
        this.pipeline = pipeline;
        this.outputStream = outputStream;
        int colon = uri.indexOf(':');
        this.protocol = uri.substring(0, colon);
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
            this.pipeline.process(this.environment);
        } catch (ProcessingException e) {
            throw new SourceException(e);
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
        if (this.systemIdForCaching == null) {
            String eventPipelineKey = this.pipeline.getKeyForEventPipeline();
            if (eventPipelineKey != null) {
                StringBuilder buffer = new StringBuilder(this.uri);
                if (this.uri.indexOf('?') == -1) {
                    buffer.append('?');
                } else {
                    buffer.append('&');
                }
                buffer.append("pipelinehash=");
                buffer.append(eventPipelineKey);
                this.systemIdForCaching = buffer.toString();
            } else {
                this.systemIdForCaching = this.uri;
            }
        }
        return this.systemIdForCaching;
    }

    /**
     * Get the validity object. This wraps validity of the enclosed event
     * pipeline. If pipeline is not cacheable, <code>null</code> is returned.
     */
    public SourceValidity getValidity() {
        if (this.validity == null) {
            this.validity = this.pipeline.getValidityForEventPipeline();
        }
        return this.validity;
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
            this.pipeline.process(this.environment, consumer);
        } catch (ProcessingException e) {
            throw new SourceException(e);
        }
    }
}
