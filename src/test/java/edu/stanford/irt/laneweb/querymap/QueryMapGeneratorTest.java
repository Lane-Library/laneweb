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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.xml.AbstractXMLConsumer;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapGeneratorTest {

    private QueryMapGenerator generator;

    private Parameters parameters;

    private QueryMapper queryMapper;

    private XMLConsumer consumer;

    @Before
    public void setUp() throws Exception {
        this.generator = new QueryMapGenerator();
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.consumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testSetQueryMapper() {
        try {
            this.generator.setQueryMapper(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.generator.setQueryMapper(this.queryMapper);
    }

    @Test
    public void testSetup() throws MalformedURLException, IOException {

        try {
            this.generator.setup(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
        replay(this.parameters);
        try {
            this.generator.setup(null, null, null, this.parameters);
        } catch (IllegalStateException e) {
        }
        this.generator.setQueryMapper(this.queryMapper);
        this.generator.setup(null, null, null, this.parameters);
        verify(this.parameters);
    }

    @Test
    public void testGenerate() throws SAXException, MalformedURLException, IOException {
        Descriptor descriptor = createMock(Descriptor.class);
        expect(descriptor.getDescriptorName()).andReturn("yomama");
        expect(descriptor.getDescriptorName()).andReturn("mama");
        replay(descriptor);
        ResourceMap resourceMap = createMock(ResourceMap.class);
        expect(resourceMap.getDescriptor()).andReturn(descriptor);
        expect(resourceMap.getResources()).andReturn(Collections.<Resource> singleton(new Resource("a", "b")));
        replay(resourceMap);
        QueryMap queryMap = createMock(QueryMap.class);
        expect(queryMap.getQuery()).andReturn("dvt");
        expect(queryMap.getDescriptor()).andReturn(descriptor);
        expect(queryMap.getResourceMap()).andReturn(resourceMap);
        expect(queryMap.getTreePath()).andReturn(null);
        expect(queryMap.getFrequencies()).andReturn(null);
        replay(queryMap);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(queryMap);
        replay(this.queryMapper);
        this.generator.setQueryMapper(this.queryMapper);
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
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

    // TODO I don't know if this actually does what I want it to.
    @Test
    public void testThreads() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        edu.stanford.irt.querymap.QueryMapper fauxQueryMapper = new edu.stanford.irt.querymap.QueryMapper() {

            @Override
            public QueryMap getQueryMap(final String query) {
                Descriptor descriptor = new Descriptor(query, query, Collections.<String> singleton(query));
                return new QueryMap(query, descriptor, new ResourceMap(descriptor, Collections.<Resource> singleton(new Resource(query,
                        query))), null, null);
            }

            // TODO: need to more thoroughly test the source reloading:
            @Override
            public QueryMap getQueryMap(final String query, final Map<String, Set<Resource>> resourceMaps,
                    final Map<String, Float> descriptorWeights, final int abstractCount) {
                return null;
            }
        };
        this.generator.setQueryMapper(fauxQueryMapper);
        for (int i = 99; i > -1; i--) {
            final String response = Integer.toString(i);
            executor.execute(new Runnable() {

                public void run() {
                    Parameters params = createMock(Parameters.class);
                    expect(params.getParameter("query", null)).andReturn(response);
                    expect(params.getParameter("resource-maps", null)).andReturn(null);
                    expect(params.getParameter("descriptor-weights", null)).andReturn(null);
                    expect(params.getParameterAsInteger("abstract-count", 100)).andReturn(null);
                    replay(params);
                    QueryMapGeneratorTest.this.generator.setup(null, null, null, params);
                    QueryMapGeneratorTest.this.generator.setConsumer(new AbstractXMLConsumer() {

                        @Override
                        public void characters(final char[] chars, final int start, final int length) {
                            assertEquals(response, new String(chars, start, length));

                        }

                        @Override
                        public void startElement(final String ns, final String localName, final String aName, final Attributes atts) {
                            if (atts.getLength() > 0) {
                                assertEquals(response, atts.getValue(0));
                            }
                        }
                    });
                    try {
                        Thread.sleep(Long.parseLong(response));
                        QueryMapGeneratorTest.this.generator.generate();
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
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
