package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapReaderTest {

    private Descriptor descriptor;

    private Map<String, Object> model;

    private OutputStream outputStream;

    private Parameters parameters;

    private QueryMap queryMap;

    private QueryMapper queryMapper;

    private QueryMapReader reader;

    private ResourceMap resourceMap;

    @Before
    public void setUp() throws Exception {
        this.reader = new QueryMapReader();
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.outputStream = createMock(OutputStream.class);
        this.model = new HashMap<String, Object>();
        this.descriptor = createMock(Descriptor.class);
        this.resourceMap = createMock(ResourceMap.class);
        this.queryMap = createMock(QueryMap.class);
    }

    @Test
    public void testGenerate() throws SAXException, MalformedURLException, IOException {
        expect(this.descriptor.getDescriptorName()).andReturn("yomama");
        expect(this.descriptor.getDescriptorName()).andReturn("mama");
        expect(this.resourceMap.getDescriptor()).andReturn(this.descriptor);
        expect(this.resourceMap.getResources()).andReturn(Collections.<Resource> singleton(new Resource("a", "b")));
        expect(this.queryMap.getQuery()).andReturn("dvt");
        expect(this.queryMap.getDescriptor()).andReturn(this.descriptor);
        expect(this.queryMap.getResourceMap()).andReturn(this.resourceMap);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(this.queryMap);
        this.reader.setQueryMapper(this.queryMapper);
        this.model.put(Model.QUERY, "dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameter("abstract-count", null)).andReturn(null);
        replayMocks();
        this.reader.setOutputStream(this.outputStream);
        this.reader.setup(null, this.model, null, this.parameters);
        this.reader.generate();
        verifyMocks();
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

    private void replayMocks() {
        replay(this.parameters);
        replay(this.queryMapper);
        replay(this.descriptor);
        replay(this.resourceMap);
        replay(this.queryMap);
    }

    private void verifyMocks() {
        verify(this.parameters);
        verify(this.queryMapper);
        verify(this.descriptor);
        verify(this.resourceMap);
        verify(this.queryMap);
    }
}
