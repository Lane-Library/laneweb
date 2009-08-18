package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapGeneratorTest {

    private XMLConsumer consumer;

    private QueryMapGenerator generator;

    private Parameters parameters;

    private QueryMapper queryMapper;

    @Before
    public void setUp() throws Exception {
        this.generator = new QueryMapGenerator();
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.consumer = createMock(XMLConsumer.class);
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
}
