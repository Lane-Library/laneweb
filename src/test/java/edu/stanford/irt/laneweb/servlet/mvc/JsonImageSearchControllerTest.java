package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class JsonImageSearchControllerTest {

    private JsonImageSearchController controller;

    private FacetFieldEntry facetFieldEntry;

    private Page<FacetFieldEntry> facetFieldEntryPage;

    private FacetPage<Image> facetPage;

    private Image image;

    private SolrImageService service;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(SolrImageService.class);
        this.controller = new JsonImageSearchController(this.service);
        this.image = createMock(Image.class);
        this.facetPage = createMock(FacetPage.class);
        this.facetFieldEntryPage = createMock(Page.class);
        this.facetFieldEntry = createMock(FacetFieldEntry.class);
    }

    @Test
    public void testGetImage() {
        expect(this.service.findById("id/id")).andReturn(this.image);
        replay(this.service);
        assertSame(this.image, this.controller.getImage("id/id"));
        verify(this.service);
    }

    @Test
    public void testGetImageFacet() {
        expect(this.service.facetOnCopyright("query")).andReturn(this.facetPage);
        expect(this.facetPage.getFacetResultPage("copyright")).andReturn(this.facetFieldEntryPage);
        expect(this.facetFieldEntryPage.getContent()).andReturn(Collections.singletonList(this.facetFieldEntry));
        replay(this.service, this.facetPage, this.facetFieldEntryPage, this.facetFieldEntry);
        assertSame(this.facetFieldEntry, this.controller.getImageFacet("query").get(0));
        verify(this.service, this.facetPage, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test
    public void testUpdateImage() {
        expect(this.service.adminFindById("id/id")).andReturn(this.image);
        this.image.setWebsiteId("id");
        expect(this.image.isEnable()).andReturn(true);
        this.image.setEnable(false);
        this.service.saveImage(this.image);
        replay(this.service, this.image);
        assertSame(this.image, this.controller.updateImage("id/id"));
        verify(this.service, this.image);
    }

    @Test
    public void testUpdateImageNotEnabled() {
        expect(this.service.adminFindById("id/id")).andReturn(this.image);
        this.image.setWebsiteId("id");
        expect(this.image.isEnable()).andReturn(false);
        this.image.setEnable(true);
        this.service.saveImage(this.image);
        replay(this.service, this.image);
        assertSame(this.image, this.controller.updateImage("id/id"));
        verify(this.service, this.image);
    }
}
