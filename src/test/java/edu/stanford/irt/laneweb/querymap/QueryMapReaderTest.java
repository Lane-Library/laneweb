package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapReaderTest {

    private OutputStream outputStream;

    private Parameters parameters;

    private QueryMapper queryMapper;

    private QueryMapReader reader;
    
    private ObjectModel objectModel;
    
    private Map laneweb;
    
    private Descriptor descriptor;
    
    private ResourceMap resourceMap;
    
    private QueryMap queryMap;

    @Before
    public void setUp() throws Exception {
        this.reader = new QueryMapReader();
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.outputStream = createMock(OutputStream.class);
        this.objectModel = createMock(ObjectModel.class);
        this.laneweb = createMock(Map.class);
        this.descriptor = createMock(Descriptor.class);
        this.resourceMap = createMock(ResourceMap.class);
        this.queryMap = createMock(QueryMap.class);
        expect(this.objectModel.get("laneweb")).andReturn(this.laneweb);
        replay(this.objectModel);
        this.reader.setObjectModel(this.objectModel);
        reset(this.objectModel);
    }

    @Test
    public void testGenerate() throws SAXException, MalformedURLException, IOException {
        expect(descriptor.getDescriptorName()).andReturn("yomama");
        expect(descriptor.getDescriptorName()).andReturn("mama");
        expect(resourceMap.getDescriptor()).andReturn(descriptor);
        expect(resourceMap.getResources()).andReturn(Collections.<Resource> singleton(new Resource("a", "b")));
        expect(queryMap.getQuery()).andReturn("dvt");
        expect(queryMap.getDescriptor()).andReturn(descriptor);
        expect(queryMap.getResourceMap()).andReturn(resourceMap);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(queryMap);
        this.reader.setQueryMapper(this.queryMapper);
        expect(this.laneweb.get(LanewebObjectModel.QUERY)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
        replayMocks();
        this.reader.setOutputStream(this.outputStream);
        this.reader.setup(null, null, null, this.parameters);
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
        replay(this.objectModel);
        replay(this.laneweb);
        replay(this.descriptor);
        replay(this.resourceMap);
        replay(this.queryMap);
    }
    
    private void verifyMocks() {
        verify(this.parameters);
        verify(this.queryMapper);
        verify(this.objectModel);
        verify(this.laneweb);
        verify(this.descriptor);
        verify(this.resourceMap);
        verify(this.queryMap);
    }
}
