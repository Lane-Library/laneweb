package edu.stanford.irt.laneweb.search.saxstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

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

    private FacetFieldEntry facedFieldEntry;

    private Page<FacetFieldEntry> facetEntry;

    private Image image;

    private Map<String, Object> model = new HashMap<String, Object>();

    private Page<Image> page;

    private SolrImageSearchSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.strategy = new SolrImageSearchSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.page = createMock(Page.class);
        this.image = createMock(Image.class);
        this.facetEntry = createMock(Page.class);
        this.facedFieldEntry = createMock(FacetFieldEntry.class);
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
        expect(this.facetEntry.getNumberOfElements()).andReturn(0);
        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
        expect(this.facedFieldEntry.getValue()).andReturn("Bassett");
        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(2);
        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
        this.model.put("path", "/path");
        this.model.put("page", this.page);
        this.model.put(Model.SOURCE, "source");
        this.model.put(Model.QUERY, "query");
        this.model.put("websiteIdFacet", this.facetEntry);
        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
    }

//    @Test
//    public void testToSAXFirstPage() throws IOException {
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(1).times(1);
//        expect(this.page.getNumber()).andReturn(0).times(6);
//        expect(this.page.getSize()).andReturn(10).times(2);
//        expect(this.page.getTotalPages()).andReturn(2).times(6);
//        expect(this.page.getTotalElements()).andReturn(2L).times(1);
//        expect(this.page.hasContent()).andReturn(true).times(1);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getTitle()).andReturn("title");
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn("src").times(4);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(1);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett").times(4);
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(4);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("path", "/path");
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(
//                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-firstPage.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test
//    public void testToSAXLastPage() throws IOException {
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(1).times(1);
//        expect(this.page.getNumber()).andReturn(1).times(6);
//        expect(this.page.getSize()).andReturn(10).times(2);
//        expect(this.page.getTotalPages()).andReturn(2).times(6);
//        expect(this.page.getTotalElements()).andReturn(2L).times(1);
//        expect(this.page.hasContent()).andReturn(true).times(1);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getTitle()).andReturn("title");
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn("src").times(4);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(0);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett");
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(2);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("path", "/path");
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-lastPage.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test
//    public void testToSAXLongTitle() throws IOException {
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(2);
//        expect(this.page.getNumber()).andReturn(2).times(6);
//        expect(this.page.getSize()).andReturn(52).times(2);
//        expect(this.page.getTotalPages()).andReturn(15).times(6);
//        expect(this.page.getTotalElements()).andReturn(106L);
//        expect(this.page.hasContent()).andReturn(true);
//        expect(this.image.getTitle()).andReturn(
//                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn("src").times(4);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(1);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett").times(4);
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(4);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("path", "/path");
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(
//                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-longTitle.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test
//    public void testToSAXNoContent() throws IOException {
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(2).times(1);
//        expect(this.page.getNumber()).andReturn(2).times(6);
//        expect(this.page.getSize()).andReturn(52).times(2);
//        expect(this.page.getTotalPages()).andReturn(15).times(6);
//        expect(this.page.hasContent()).andReturn(false).times(1);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getTitle()).andReturn("title");
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn("src").times(4);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(0);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett");
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(2);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("path", "/path");
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAXNoContent.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test
//    public void testToSAXNoPagination() throws IOException {
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(1).times(1);
//        expect(this.page.getNumber()).andReturn(1).times(2);
//        expect(this.page.getSize()).andReturn(0).times(2);
//        expect(this.page.getTotalPages()).andReturn(0).times(2);
//        expect(this.page.getTotalElements()).andReturn(1L).times(1);
//        expect(this.page.hasContent()).andReturn(true).times(1);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getTitle()).andReturn("title");
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn("src").times(4);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(1);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett").times(4);
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(4);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(
//                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-nopagination.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test
//    public void testToSAXNullSource() throws IOException {
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(2).times(1);
//        expect(this.page.getNumber()).andReturn(2).times(6);
//        expect(this.page.getSize()).andReturn(52).times(2);
//        expect(this.page.getTotalPages()).andReturn(15).times(6);
//        expect(this.page.getTotalElements()).andReturn(106L).times(1);
//        expect(this.page.hasContent()).andReturn(true).times(1);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getTitle()).andReturn("title");
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn(null).times(2);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(0);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett");
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(2);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("path", "/path");
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(
//                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAXNullSource.xml"),
//                this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test
//    public void testToSAXSelectedResource() throws IOException {
//        this.model.put("selectedResource", "foo");
//        expect(this.page.getContent()).andReturn(Collections.singletonList(this.image));
//        expect(this.page.getNumberOfElements()).andReturn(2).times(1);
//        expect(this.page.getNumber()).andReturn(2).times(6);
//        expect(this.page.getSize()).andReturn(52).times(2);
//        expect(this.page.getTotalPages()).andReturn(15).times(6);
//        expect(this.page.getTotalElements()).andReturn(106L).times(1);
//        expect(this.page.hasContent()).andReturn(true).times(1);
//        expect(this.image.getId()).andReturn("id/andSlash").times(7);
//        expect(this.image.getTitle()).andReturn("title");
//        expect(this.image.getCopyrightText()).andReturn("copyrightText").times(2);
//        expect(this.image.getPageUrl()).andReturn("pageUrl").times(2);
//        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
//        expect(this.image.getSrc()).andReturn("src").times(4);
//        expect(this.facetEntry.getNumberOfElements()).andReturn(0);
//        expect(this.facetEntry.getContent()).andReturn(Collections.singletonList(this.facedFieldEntry));
//        expect(this.facedFieldEntry.getValue()).andReturn("Bassett");
//        expect(this.facedFieldEntry.getValueCount()).andReturn((long) 2).times(2);
//        replay(this.page, this.image, this.facetEntry, this.facedFieldEntry);
//        this.model.put("path", "/path");
//        this.model.put("page", this.page);
//        this.model.put(Model.SOURCE, "source");
//        this.model.put(Model.QUERY, "query");
//        this.model.put("websiteIdFacet", this.facetEntry);
//        this.strategy.toSAX(this.model, this.xmlConsumer);
//        assertEquals(this.xmlConsumer.getExpectedResult(this,
//                "SolrImageSearchSAXStrategyTest-testToSAXSelectedResource.xml"), this.xmlConsumer.getStringValue());
//        verify(this.page, this.image);
//    }
//
//    @Test(expected = LanewebException.class)
//    public void testToSAXThrowsException() throws SAXException {
//        XMLConsumer consumer = createMock(XMLConsumer.class);
//        consumer.startDocument();
//        expectLastCall().andThrow(new SAXException());
//        replay(consumer);
//        this.strategy.toSAX(null, consumer);
//        verify(consumer);
//    }
}
