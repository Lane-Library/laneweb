package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategyTest {

    private FacetFieldEntry facetFieldEntry;

    private Page<FacetFieldEntry> facetFieldEntryPage;

    private Map<String, String> idMapping;

    private Image image;

    private Page<Image> imagePage;

    private SolrImageSearchResult result;

    private SolrImageSearchSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.idMapping = Collections.singletonMap("id", "mappedId");
        this.strategy = new SolrImageSearchSAXStrategy(this.idMapping);
        this.xmlConsumer = new TestXMLConsumer();
        this.imagePage = createMock(Page.class);
        this.image = createMock(Image.class);
        this.facetFieldEntryPage = createMock(Page.class);
        this.facetFieldEntry = createMock(FacetFieldEntry.class);
        this.result = createMock(SolrImageSearchResult.class);
    }

    @Test
    public void testToSAX() throws IOException {
        expect(this.result.getPage()).andReturn(this.imagePage);
        expect(this.result.getQuery()).andReturn("query").times(2);
        expect(this.result.getFacet()).andReturn(this.facetFieldEntryPage);
        expect(this.result.getPath()).andReturn("/path").times(2);
        expect(this.result.getSelectedResource()).andReturn(null).times(4);
        expect(this.result.getSource()).andReturn("source").times(2);
        expect(this.imagePage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.imagePage.getNumberOfElements()).andReturn(2);
        expect(this.imagePage.getNumber()).andReturn(2).times(6);
        expect(this.imagePage.getSize()).andReturn(52).times(2);
        expect(this.imagePage.getTotalPages()).andReturn(15).times(6);
        expect(this.imagePage.getTotalElements()).andReturn(106L);
        expect(this.imagePage.hasContent()).andReturn(true);
        expect(this.image.getId()).andReturn("id/andSlash");
        expect(this.image.getPageUrl()).andReturn("pageUrl");
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.facetFieldEntryPage.getNumberOfElements()).andReturn(0);
        replay(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test
    public void testToSAXFirstPage() throws IOException {
        expect(this.result.getPage()).andReturn(this.imagePage);
        expect(this.result.getQuery()).andReturn("query").times(2);
        expect(this.result.getFacet()).andReturn(this.facetFieldEntryPage).times(2);
        expect(this.result.getPath()).andReturn("/path").times(3);
        expect(this.result.getSelectedResource()).andReturn(null).times(5);
        expect(this.result.getSource()).andReturn("source").times(2);
        expect(this.imagePage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.imagePage.getNumberOfElements()).andReturn(1);
        expect(this.imagePage.getNumber()).andReturn(0).times(6);
        expect(this.imagePage.getSize()).andReturn(10).times(2);
        expect(this.imagePage.getTotalPages()).andReturn(2).times(6);
        expect(this.imagePage.getTotalElements()).andReturn(2L);
        expect(this.imagePage.hasContent()).andReturn(true);
        expect(this.image.getId()).andReturn("id/andSlash");
        expect(this.image.getPageUrl()).andReturn("pageUrl");
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.facetFieldEntryPage.getNumberOfElements()).andReturn(1).times(2);
        expect(this.facetFieldEntryPage.getContent()).andReturn(Collections.singletonList(this.facetFieldEntry))
                .times(2);
        expect(this.facetFieldEntry.getValue()).andReturn("Bassett").times(4);
        expect(this.facetFieldEntry.getValueCount()).andReturn((long) 2).times(3);
        replay(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-firstPage.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test
    public void testToSAXLastPage() throws IOException {
        expect(this.result.getPage()).andReturn(this.imagePage);
        expect(this.result.getQuery()).andReturn("query").times(2);
        expect(this.result.getFacet()).andReturn(this.facetFieldEntryPage);
        expect(this.result.getPath()).andReturn("/path").times(2);
        expect(this.result.getSource()).andReturn("source").times(2);
        expect(this.result.getSelectedResource()).andReturn(null).times(4);
        expect(this.imagePage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.imagePage.getNumberOfElements()).andReturn(1);
        expect(this.imagePage.getNumber()).andReturn(1).times(6);
        expect(this.imagePage.getSize()).andReturn(10).times(2);
        expect(this.imagePage.getTotalPages()).andReturn(2).times(6);
        expect(this.imagePage.getTotalElements()).andReturn(2L);
        expect(this.imagePage.hasContent()).andReturn(true);
        expect(this.image.getId()).andReturn("id/andSlash");
        expect(this.image.getPageUrl()).andReturn("pageUrl");
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.facetFieldEntryPage.getNumberOfElements()).andReturn(0);
        replay(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-lastPage.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test
    public void testToSAXNoContent() throws IOException {
        expect(this.result.getPage()).andReturn(this.imagePage);
        expect(this.result.getTab()).andReturn(null);
        expect(this.result.getQuery()).andReturn("query");
        expect(this.result.getFacet()).andReturn(this.facetFieldEntryPage);
        expect(this.imagePage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.imagePage.getNumberOfElements()).andReturn(0);
        expect(this.imagePage.getNumber()).andReturn(0).times(2);
        expect(this.imagePage.getSize()).andReturn(0).times(2);
        expect(this.imagePage.getTotalPages()).andReturn(0).times(2);
        expect(this.imagePage.hasContent()).andReturn(false);
        expect(this.image.getId()).andReturn("id/andSlash");
        expect(this.image.getPageUrl()).andReturn("pageUrl");
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.facetFieldEntryPage.getNumberOfElements()).andReturn(0);
        replay(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAXNoContent.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test
    public void testToSAXNoPagination() throws IOException {
        expect(this.result.getPage()).andReturn(this.imagePage);
        expect(this.result.getQuery()).andReturn("query").times(2);
        expect(this.result.getFacet()).andReturn(this.facetFieldEntryPage).times(2);
        expect(this.result.getPath()).andReturn("").times(3);
        expect(this.result.getSelectedResource()).andReturn(null).times(5);
        expect(this.result.getSource()).andReturn("source").times(2);
        expect(this.imagePage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.imagePage.getNumberOfElements()).andReturn(1);
        expect(this.imagePage.getNumber()).andReturn(1).times(6);
        expect(this.imagePage.getSize()).andReturn(0).times(2);
        expect(this.imagePage.getTotalElements()).andReturn(0L);
        expect(this.imagePage.hasContent()).andReturn(true);
        expect(this.image.getId()).andReturn("id/andSlash");
        expect(this.image.getPageUrl()).andReturn("pageUrl");
        expect(this.imagePage.getTotalPages()).andReturn(2).times(6);
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.facetFieldEntryPage.getNumberOfElements()).andReturn(1).times(2);
        expect(this.facetFieldEntryPage.getContent()).andReturn(Collections.singletonList(this.facetFieldEntry))
                .times(2);
        expect(this.facetFieldEntry.getValue()).andReturn("Bassett").times(4);
        expect(this.facetFieldEntry.getValueCount()).andReturn((long) 2).times(3);
        replay(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "SolrImageSearchSAXStrategyTest-testToSAX-nopagination.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test
    public void testToSAXSelectedResource() throws IOException {
        expect(this.result.getPage()).andReturn(this.imagePage);
        expect(this.result.getQuery()).andReturn("query").times(2);
        expect(this.result.getFacet()).andReturn(this.facetFieldEntryPage);
        expect(this.result.getPath()).andReturn("/path").times(2);
        expect(this.result.getSelectedResource()).andReturn("foo").times(12);
        expect(this.result.getSource()).andReturn("source").times(2);
        expect(this.imagePage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.imagePage.getNumberOfElements()).andReturn(2);
        expect(this.imagePage.getNumber()).andReturn(2).times(6);
        expect(this.imagePage.getSize()).andReturn(52).times(2);
        expect(this.imagePage.getTotalPages()).andReturn(15).times(6);
        expect(this.imagePage.getTotalElements()).andReturn(106L);
        expect(this.imagePage.hasContent()).andReturn(true);
        expect(this.image.getId()).andReturn("id/andSlash");
        expect(this.image.getPageUrl()).andReturn("pageUrl");
        expect(this.image.getThumbnailSrc()).andReturn("thumbnailSrc");
        expect(this.facetFieldEntryPage.getNumberOfElements()).andReturn(0);
        replay(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this,
                        "SolrImageSearchSAXStrategyTest-testToSAXSelectedResource.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.imagePage, this.image, this.facetFieldEntryPage, this.facetFieldEntry);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer consumer = createMock(XMLConsumer.class);
        consumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.result, consumer);
        this.strategy.toSAX(null, consumer);
        verify(this.result, consumer);
    }
}
