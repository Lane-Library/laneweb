package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapException;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapControllerTest {

    private QueryMapController controller;

    private SolrQueryParser parser;

    private QueryMap queryMap;

    private QueryMapper queryMapper;

    private ResourceMap resourceMap;

    @Before
    public void setUp() {
        this.queryMapper = mock(QueryMapper.class);
        this.parser = mock(SolrQueryParser.class);
        this.controller = new QueryMapController(this.parser, this.queryMapper);
        this.queryMap = mock(QueryMap.class);
        this.resourceMap = mock(ResourceMap.class);
    }

    @Test
    public void testExceptionHandler() {
        assertEquals("QueryMapper failed to get resource map: edu.stanford.irt.querymap.QueryMapException: oopsie",
                this.controller.handleException(new QueryMapException("oopsie")));
    }

    @Test
    public void testGetResourceMap() {
        expect(this.parser.parse(null)).andReturn(null);
        expect(this.queryMapper.getResourceMap(null)).andReturn(this.resourceMap);
        replay(this.queryMapper, this.parser, this.queryMap, this.resourceMap);
        assertSame(this.resourceMap, this.controller.getJSONResourceMap(null));
        verify(this.queryMapper, this.parser, this.queryMap, this.resourceMap);
    }
}
