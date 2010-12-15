package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapGeneratorTest {

    private XMLConsumer consumer;

    private Descriptor descriptor;

    private QueryMapGenerator generator;

    private Map<String, Object> model;

    private Parameters parameters;

    private QueryMap queryMap;

    private QueryMapper queryMapper;

    private ResourceMap resourceMap;

    @Before
    public void setUp() throws Exception {
        this.generator = new QueryMapGenerator();
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.consumer = createMock(XMLConsumer.class);
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
        expect(this.queryMap.getTreePath()).andReturn(null);
        expect(this.queryMap.getFrequencies()).andReturn(null);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(this.queryMap);
        this.generator.setQueryMapper(this.queryMapper);
        this.model.put(Model.QUERY, "dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameter("abstract-count", null)).andReturn(null);
        this.generator.setConsumer(this.consumer);
        replayMocks();
        this.generator.setup(null, this.model, null, this.parameters);
        this.generator.generate();
        verifyMocks();
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
        } catch (IllegalStateException e) {
        }
        this.model.put(Model.QUERY, "dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameter("abstract-count", null)).andReturn(null);
        replayMocks();
        try {
            this.generator.setup(null, this.model, null, this.parameters);
        } catch (IllegalStateException e) {
        }
        this.generator.setQueryMapper(this.queryMapper);
        this.generator.setup(null, this.model, null, this.parameters);
        verifyMocks();
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
