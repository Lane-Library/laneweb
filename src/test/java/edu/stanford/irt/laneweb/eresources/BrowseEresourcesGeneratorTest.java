package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class BrowseEresourcesGeneratorTest {

    private CollectionManager collectionManager;

    private BrowseEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new BrowseEresourcesGenerator("er-browse", this.collectionManager, this.saxStrategy);
    }

    @Test
    public void testEncodedSubset() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.SUBSET, "subset%20space"));
        expect(this.collectionManager.getSubset("subset space")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=;t=;s=subset space", this.generator.createKey().toString());
        verify(this.collectionManager);
    }

    @Test
    public void testEncodedType() {
        this.generator.setModel(Collections.<String, Object> emptyMap());
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type%20space"));
        expect(this.collectionManager.getType("type space")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=all;t=type space;s=", this.generator.createKey().toString());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListNull() {
        assertEquals(0, this.generator.getEresourceList(this.collectionManager).size());
    }

    @Test
    public void testGetEresourceListType() {
        this.generator.setModel(Collections.<String, Object> emptyMap());
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListTypeAll() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "all"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=all;t=type;s=", this.generator.createKey().toString());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListTypeAlpha() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "a"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type", 'a')).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=a;t=type;s=", this.generator.createKey().toString());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListTypeAlphaString() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "abc"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type", 'a')).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=a;t=type;s=", this.generator.createKey().toString());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListTypeNoAlpha() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=all;t=type;s=", this.generator.createKey().toString());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceSubset() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.SUBSET, "subset"));
        expect(this.collectionManager.getSubset("subset")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        assertEquals("p=0;a=;t=;s=subset", this.generator.createKey().toString());
        verify(this.collectionManager);
    }
}
