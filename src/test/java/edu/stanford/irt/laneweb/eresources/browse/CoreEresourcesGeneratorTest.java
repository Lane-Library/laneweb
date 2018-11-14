package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

public class CoreEresourcesGeneratorTest {

    private CoreEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private SolrService solrService;

    @Before
    public void setUp() throws Exception {
        this.solrService = mock(SolrService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new CoreEresourcesGenerator("type", this.solrService, this.saxStrategy);
    }

    @Test
    public void testGetEresourceList() {
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.solrService.getCore("type")).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListNullType() {
        assertEquals(0, this.generator.getEresourceList(this.solrService).size());
    }

    @Test
    public void testSetModel() {
        this.generator.setModel(Collections.emptyMap());
        assertEquals("p=0;t=", this.generator.createKey().toString());
    }

    @Test
    public void testSetModelType() {
        this.generator.setModel(Collections.singletonMap(Model.TYPE, "type"));
        assertEquals("p=0;t=type", this.generator.createKey().toString());
    }

    @Test
    public void testSetParametersNoType() {
        this.generator.setParameters(Collections.emptyMap());
        assertEquals("p=0;t=", this.generator.createKey().toString());
    }
}
