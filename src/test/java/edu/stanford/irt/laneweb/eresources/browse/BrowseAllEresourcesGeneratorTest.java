package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceBrowseService;
import edu.stanford.irt.laneweb.model.Model;

public class BrowseAllEresourcesGeneratorTest {

    private BrowseAllEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private EresourceBrowseService restBrowseService;

    @BeforeEach
    public void setUp() {
        this.restBrowseService = mock(EresourceBrowseService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new BrowseAllEresourcesGenerator("er-browse", this.restBrowseService, this.saxStrategy);
    }

    @Test
    public void testCreateKey() {
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        assertEquals("p=;a=;q=query", this.generator.createKey().toString());
    }

    @Test
    public void testGetEresourceList() {
        expect(this.restBrowseService.browseByQuery("query")).andReturn(Collections.singletonList(null));
        replay(this.restBrowseService);
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        assertEquals(1, this.generator.getEresourceList(this.restBrowseService).size());
        verify(this.restBrowseService);
    }

    @Test
    public void testGetEresourceListNoType() {
        this.generator.setParameters(Collections.emptyMap());
        assertEquals(0, this.generator.getEresourceList(this.restBrowseService).size());
    }

    @Test
    public void testGetHeading() {
        assertNull(this.generator.getHeading());
    }
}
