package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapReaderTest {

    private QueryMapReader reader;

    private Parameters parameters;

    private QueryMapper queryMapper;

    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        this.reader = new QueryMapReader();
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.outputStream = createMock(OutputStream.class);
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
        replay(queryMap);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(queryMap);
        replay(this.queryMapper);
        this.reader.setQueryMapper(this.queryMapper);
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
        replay(this.parameters);
        this.reader.setOutputStream(this.outputStream);
        this.reader.setup(null, null, null, this.parameters);
        this.reader.generate();
        verify(descriptor);
        verify(resourceMap);
        verify(queryMap);
        verify(this.queryMapper);
        verify(this.parameters);
    }

    @Test
    public void testSetConsumer() throws IOException {
        try {
            this.reader.setOutputStream(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.reader.setOutputStream(this.outputStream);
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
                // TODO Auto-generated method stub
                return null;
            }
        };
        this.reader.setQueryMapper(fauxQueryMapper);
        for (int i = 999; i > -1; i--) {
            final String response = Integer.toString(i);
            executor.execute(new Runnable() {

                public void run() {
                    Parameters params = createMock(Parameters.class);
                    expect(params.getParameter("query", null)).andReturn(response);
                    expect(params.getParameter("resource-maps", null)).andReturn(null);
                    expect(params.getParameter("descriptor-weights", null)).andReturn(null);
                    expect(params.getParameterAsInteger("abstract-count", 100)).andReturn(null);
                    replay(params);
                    QueryMapReaderTest.this.reader.setup(null, null, null, params);
                    try {
                        QueryMapReaderTest.this.reader.setOutputStream(new OutputStream() {

                            @Override
                            public void write(final int b) throws IOException {
                                throw new IOException("not implemented");
                            }

                            @Override
                            public void write(final byte[] bytes) {
                                assertTrue(new String(bytes).indexOf(response) == 10);
                            }
                        });
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    try {
                        Thread.sleep(Long.parseLong(response));
                        QueryMapReaderTest.this.reader.generate();
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    verify(params);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
