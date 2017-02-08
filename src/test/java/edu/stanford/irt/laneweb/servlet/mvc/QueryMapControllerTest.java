package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.ResourceMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ QueryMapper.class, SolrQueryParser.class })
public class QueryMapControllerTest {

    private QueryMapController controller;

    private SolrQueryParser parser;

    private QueryMap queryMap;

    private QueryMapper queryMapper;

    private ResourceMap resourceMap;

    @Before
    public void setUp() {
        this.queryMapper = PowerMock.createMock(QueryMapper.class);
        this.parser = PowerMock.createMock(SolrQueryParser.class);
        this.controller = new QueryMapController(this.parser, this.queryMapper);
        this.queryMap = createMock(QueryMap.class);
        this.resourceMap = createMock(ResourceMap.class);
    }

    @Test
    public void testGetJSONResourceMap() {
        expect(this.parser.parse(null)).andReturn(null);
        expect(this.queryMapper.getQueryMap(null)).andReturn(this.queryMap);
        expect(this.queryMap.getResourceMap()).andReturn(this.resourceMap);
        PowerMock.replay(this.queryMapper, this.parser, this.queryMap, this.resourceMap);
        assertSame(this.resourceMap, this.controller.getJSONResourceMap(null));
        PowerMock.verify(this.queryMapper, this.parser, this.queryMap, this.resourceMap);
    }
}
