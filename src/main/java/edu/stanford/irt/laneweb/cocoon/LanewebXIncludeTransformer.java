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
package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.impl.MultiSourceValidity;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * shamelessly copied from cocoon 2.2, modified to use fallback if
 * ProcessingException or SAXException
 */
public class LanewebXIncludeTransformer extends AbstractTransformer implements CacheableProcessingComponent {

    private static final String XINCLUDE_CACHE_KEY = "XInclude";

    private XIncludePipe xIncludePipe;

    protected ServiceManager manager;

    protected SAXParser parser;

    protected SourceResolver resolver;

    /** The {@link SourceValidity} instance associated with this request. */
    protected MultiSourceValidity validity;

    /** Key to be used for caching */
    public Serializable getKey() {
        return XINCLUDE_CACHE_KEY;
    }

    /** Get the validity for this transform */
    public SourceValidity getValidity() {
        return this.validity;
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

    public void setParser(final SAXParser parser) {
        this.parser = parser;
    }

    public void setServiceManager(final ServiceManager serviceManager) {
        this.manager = serviceManager;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters parameters) {
        this.resolver = resolver;
        this.validity = new MultiSourceValidity(resolver, MultiSourceValidity.CHECK_ALWAYS);
        this.xIncludePipe = new XIncludePipe(this.resolver, this.validity, this.manager, this.parser);
        this.xIncludePipe.init(null, null);
        super.setContentHandler(this.xIncludePipe);
        super.setLexicalHandler(this.xIncludePipe);
    }
}
