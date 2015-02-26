package edu.stanford.irt.laneweb.search.saxstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategyTest {

    private TestXMLConsumer xmlConsumer;

    private SolrImageSearchSAXStrategy strategy;

    private Map<String, Object> model = new HashMap<String, Object>();

    private Page<Image> page;

    private Image image;

    private Page<FacetFieldEntry> facetEntry;
    private FacetFieldEntry facedFieldEntry;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.strategy = new SolrImageSearchSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.page = createMock(Page.class);
        this.image = createMock(Image.class);
        this.facetEntry = createMock(Page.class);
        this .facedFieldEntry = createMock(FacetFieldEntry.class);
    }

    @Test
    public void testToSAX() throws IOException {
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.page.getNumberOfElements()).andReturn(2).times(1);
        expect(this.page.getNumber()).andReturn(2).times(6);
        expect(this.page.getSize()).andReturn(52).times(2);
        expect(this.page.getTotalPages()).andReturn(15).times(6);
        expect(this.page.getTotalElements()).andReturn(106L).times(1);
        expect(this.page.hasContent()).andReturn(true).times(1);
        expect(this.image.getId()).andReturn("id/andSlash").times(7);
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.image.getSrc()).andReturn("src").times(4);
        expect( this.facetEntry.getNumberOfElements()).andReturn(0);
        expect(facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
        replay(this.page, this.image, this.facetEntry);
        this.model.put("path", "/path");
        this.model.put("page", page);
        this.model.put(Model.SOURCE, "source");
        this.model.put(Model.QUERY, "query");
        this.model.put("websiteIdFacet", this.facetEntry);
        this.strategy.toSAX(this.model, this.xmlConsumer);
        
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.image);
    }

    @Test
    public void testToSAXNoPagination() throws IOException {
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.page.getNumberOfElements()).andReturn(1).times(1);
        expect(this.page.getNumber()).andReturn(1).times(2);
        expect(this.page.getSize()).andReturn(0).times(2);
        expect(this.page.getTotalPages()).andReturn(0).times(2);
        expect(this.page.getTotalElements()).andReturn(1L).times(1);
        expect(this.page.hasContent()).andReturn(true).times(1);
        expect(this.image.getId()).andReturn("id/andSlash").times(7);
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.image.getSrc()).andReturn("src").times(4);
        expect( this.facetEntry.getNumberOfElements()).andReturn(1);
        expect(facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
        replay(this.page, this.image, this.facetEntry);
        this.model.put("page", page);
        this.model.put(Model.SOURCE, "source");
        this.model.put(Model.QUERY, "query");
        this.model.put("websiteIdFacet", this.facetEntry);
        this.strategy.toSAX(this.model, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-nopagination.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.image);
    }

    @Test
    public void testToSAXLongTitle() throws IOException {
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.page.getNumberOfElements()).andReturn(2);
        expect(this.page.getNumber()).andReturn(2).times(6);
        expect(this.page.getSize()).andReturn(52).times(2);
        expect(this.page.getTotalPages()).andReturn(15).times(6);
        expect(this.page.getTotalElements()).andReturn(106L);
        expect(this.page.hasContent()).andReturn(true);
        expect(this.image.getTitle()).andReturn(
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.image.getSrc()).andReturn("src").times(4);
        expect(this.image.getId()).andReturn("id/andSlash").times(7);
        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
        expect( this.facetEntry.getNumberOfElements()).andReturn(1);
        expect(facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
        replay(this.page, this.image, this.facetEntry);
        this.model.put("page", page);
        this.model.put(Model.SOURCE, "source");
        this.model.put(Model.QUERY, "query");
        this.model.put("websiteIdFacet", this.facetEntry);
        this.strategy.toSAX(this.model, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-longTitle.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.image);
    }

    @Test
    public void testToSAXFirstPage() throws IOException {
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.page.getNumberOfElements()).andReturn(1).times(1);
        expect(this.page.getNumber()).andReturn(0).times(6);
        expect(this.page.getSize()).andReturn(10).times(2);
        expect(this.page.getTotalPages()).andReturn(2).times(6);
        expect(this.page.getTotalElements()).andReturn(2L).times(1);
        expect(this.page.hasContent()).andReturn(true).times(1);
        expect(this.image.getId()).andReturn("id/andSlash").times(7);
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.image.getSrc()).andReturn("src").times(4);  
        expect( this.facetEntry.getNumberOfElements()).andReturn(1);
        expect(facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
        replay(this.page, this.image, this.facetEntry);
        this.model.put("path", "/path");
        this.model.put("page", page);
        this.model.put(Model.SOURCE, "source");
        this.model.put(Model.QUERY, "query");
        this.model.put("websiteIdFacet", this.facetEntry);
        this.strategy.toSAX(this.model, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-firstPage.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.image);
    }

    @Test
    public void testToSAXLastPage() throws IOException {
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.page.getNumberOfElements()).andReturn(1).times(1);
        expect(this.page.getNumber()).andReturn(1).times(6);
        expect(this.page.getSize()).andReturn(10).times(2);
        expect(this.page.getTotalPages()).andReturn(2).times(6);
        expect(this.page.getTotalElements()).andReturn(2L).times(1);
        expect(this.page.hasContent()).andReturn(true).times(1);
        expect(this.image.getId()).andReturn("id/andSlash").times(7);
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.image.getSrc()).andReturn("src").times(4);  expect( this.facetEntry.getNumberOfElements()).andReturn(0);
        expect(facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
        replay(this.page, this.image, this.facetEntry);
        this.model.put("path", "/path");
        this.model.put("page", page);
        this.model.put(Model.SOURCE, "source");
        this.model.put(Model.QUERY, "query");
        this.model.put("websiteIdFacet", this.facetEntry);
        this.strategy.toSAX(this.model, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-lastPage.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.image);
    }
}
