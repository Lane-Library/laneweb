package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrAdminImageSearchGeneratorTest {

    private FacetPage<Image> facetPage;

    private SolrAdminImageSearchGenerator generator;

    private Page<Image> page;

    private SAXStrategy<SolrImageSearchResult> saxStrategy;

    private SolrImageService service;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.saxStrategy = mock(SAXStrategy.class);
        this.service = mock(SolrImageService.class);
        this.generator = new SolrAdminImageSearchGenerator(this.service, this.saxStrategy);
        this.page = mock(Page.class);
        this.facetPage = mock(FacetPage.class);
    }

    @Test
    public void testDoSearchString() {
        expect(this.service.adminFindByTitleAndDescription(eq("query"), eq("0"), isNull(), isA(PageRequest.class)))
                .andReturn(this.page);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        expect(this.facetPage.getFacetResultPage("websiteId")).andReturn(null);
        replay(this.saxStrategy, this.service, this.page, this.facetPage);
        this.generator.setModel(Collections.singletonMap(Model.BASE_PATH, "base-path"));
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.service, this.page, this.facetPage);
    }

    @Test
    public void testGetPage() {
        expect(this.service.adminFindByTitleAndDescription(eq("query"), eq("0"), isNull(), isA(PageRequest.class)))
                .andReturn(this.page);
        replay(this.saxStrategy, this.service, this.page);
        this.generator.setModel(Collections.singletonMap(Model.BASE_PATH, "base-path"));
        this.generator.getPage("query");
        verify(this.saxStrategy, this.service, this.page);
    }

    @Test
    public void testGetPageLimit() {
        java.util.Date.from(LocalDate.parse("1969-05-05").atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant());
        expect(this.service.adminFindAllFilterOnCopyrightAndWebsiteIdAndDate(eq("query"), eq("0"), isNull(),
                eq(java.util.Date.from(
                        LocalDate.parse("1969-05-05").atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant())),
                isA(PageRequest.class))).andReturn(this.page);
        replay(this.saxStrategy, this.service, this.page);
        Map<String, Object> model = new HashMap<>();
        model.put(Model.BASE_PATH, "base-path");
        model.put(Model.LIMIT, "1969/05/05");
        this.generator.setModel(model);
        this.generator.getPage("query");
        verify(this.saxStrategy, this.service, this.page);
    }
}
