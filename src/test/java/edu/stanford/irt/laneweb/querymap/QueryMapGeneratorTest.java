package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.xml.AbstractXMLConsumer;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapGeneratorTest {

    private QueryMapGenerator generator;

    private ServiceManager serviceManager;

    private Parameters parameters;
    
    private QueryMapper queryMapper;
    
    private XMLConsumer consumer;

    @Before
    public void setUp() throws Exception {
        this.generator = new QueryMapGenerator();
        this.serviceManager = createMock(ServiceManager.class);
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.consumer = createMock(XMLConsumer.class);
    }
    
    @Test
    public void testSetQueryMapper() {
        try {
            this.generator.setQueryMapper(null);
            fail();
        } catch(IllegalArgumentException e) {}
        this.generator.setQueryMapper(this.queryMapper);
    }

    @Test
    public void testService() throws ServiceException {
        try {
            this.generator.service(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.serviceManager.lookup(QueryMapper.ROLE)).andReturn(this.queryMapper);
        replay(this.serviceManager);
        this.generator.service(this.serviceManager);
        verify(this.serviceManager);
    }

    @Test
    public void testSetup() throws MalformedURLException, IOException {
        try {
            this.generator.setup(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        replay(this.parameters);
        this.generator.setup(null, null, null, this.parameters);
        verify(this.parameters);
    }

    @Test
    public void testGenerate() throws SAXException, MalformedURLException,
            IOException {
        Descriptor descriptor = createMock(Descriptor.class);
        expect(descriptor.getDescriptorName()).andReturn("yomama");
        expect(descriptor.getDescriptorName()).andReturn("mama");
        replay(descriptor);
        ResourceMap resourceMap = createMock(ResourceMap.class);
        expect(resourceMap.getDescriptor()).andReturn(descriptor);
        expect(resourceMap.getResources()).andReturn(Collections.<String>singleton("yo"));
        replay(resourceMap);
        QueryMap queryMap = createMock(QueryMap.class);
        expect(queryMap.getQuery()).andReturn("dvt");
        expect(queryMap.getDescriptor()).andReturn(descriptor);
        expect(queryMap.getResourceMap()).andReturn(resourceMap);
        replay(queryMap);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(queryMap);
        replay(this.queryMapper);
        this.generator.setQueryMapper(this.queryMapper);
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        replay(this.parameters);
        this.generator.setConsumer(this.consumer);
        this.generator.setup(null, null, null, this.parameters);
        this.generator.generate();
        verify(descriptor);
        verify(resourceMap);
        verify(queryMap);
        verify(this.queryMapper);
        verify(this.parameters);
    }

    @Test
    public void testSetConsumer() {
        try {
            this.generator.setConsumer(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.generator.setConsumer(this.consumer);
    }
    
    //TODO I don't know if this actually does what I want it to.
    @Test
    public void testThreads() throws ServiceException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(100);
        this.generator.setQueryMapper(this.queryMapper);
        final String[] response = new String[1000];
        for (int i = 0; i < 1000; i++) {
            response[i] = Integer.toString(i);
            Descriptor descriptor = createMock(Descriptor.class);
            expect(descriptor.getDescriptorName()).andReturn(response[i]);
            expect(descriptor.getDescriptorName()).andReturn(response[i]);
            replay(descriptor);
            ResourceMap resourceMap = createMock(ResourceMap.class);
            expect(resourceMap.getDescriptor()).andReturn(descriptor);
            expect(resourceMap.getResources()).andReturn(Collections.<String>singleton(response[i]));
            replay(resourceMap);
            QueryMap queryMap = createMock(QueryMap.class);
            expect(queryMap.getQuery()).andReturn(response[i]);
            expect(queryMap.getDescriptor()).andReturn(descriptor);
            expect(queryMap.getResourceMap()).andReturn(resourceMap);
            replay(queryMap);
            expect(this.queryMapper.getQueryMap(response[i])).andReturn(queryMap);
        }
        replay(this.queryMapper);
        for (int k = 0; k < 1000; k++) {
            final int i = k;
            executor.execute(new Runnable() {

                public void run() {
                    Parameters params = createMock(Parameters.class);
                    expect(params.getParameter("query", null)).andReturn(response[i]);
                    replay(params);
                    QueryMapGeneratorTest.this.generator.setup(null, null,
                            null, params);
                    QueryMapGeneratorTest.this.generator.setConsumer(new AbstractXMLConsumer() {
                        public void characters(char[] chars, int start, int length) {
                            assertEquals(response[i], new String(chars, start, length));
                            
                        }
                        public void startElement(String ns, String localName, String aName, Attributes atts) {
                            if (atts.getLength() > 0) {
                                assertEquals(response[i], atts.getValue(0));
                            }
                        }
                    });
                    try {
                        QueryMapGeneratorTest.this.generator.generate();
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    }
                    verify(params);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
    }

}
