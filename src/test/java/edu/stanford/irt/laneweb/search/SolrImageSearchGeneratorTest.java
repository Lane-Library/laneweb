package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGeneratorTest {

    private SolrImageSearchGenerator generator;

    private Map<String, Object> model;

    private SAXStrategy<Page<Image>> saxStrategy;

    private SolrImageService service;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(SolrImageService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new SolrImageSearchGenerator(this.service, this.saxStrategy);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testDoSearchCategory() {
        this.model.put(Model.CATEGORY, "2");
        Capture<Pageable> pageable = new Capture<Pageable>();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("2"), capture(pageable)))
                .andReturn(null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchDefault() {
        Capture<Pageable> pageable = new Capture<Pageable>();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
                .andReturn(null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchPage() {
        this.model.put(Model.PAGE, "2");
        Capture<Pageable> pageable = new Capture<Pageable>();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
                .andReturn(null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(2, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }
}
