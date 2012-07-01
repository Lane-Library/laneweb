package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.impl.DefaultContentResult;

public class ContentResultSearchResultTest {

    private ContentResult contentResult;

    private DefaultContentResult contentResult1;

    private DefaultContentResult contentResult2;

    private ContentResultSearchResult contentResultSearchResult1;

    private ContentResultSearchResult contentResultSearchResult2;

    private Pattern queryTermPattern;

    private ContentResultSearchResult result;

    @Before
    public void setUp() {
        this.contentResult = createMock(ContentResult.class);
        this.queryTermPattern = QueryTermPattern.getPattern("query");
        expect(this.contentResult.getTitle()).andReturn("title").times(4);
        expect(this.contentResult.getId()).andReturn("id");
        expect(this.contentResult.getDescription()).andReturn("description").times(2);
        expect(this.contentResult.getPublicationDate()).andReturn("publicationDate").times(2);
        replay(this.contentResult);
        this.result = new ContentResultSearchResult(this.contentResult, "resourceHits", "resourceId", "resourceName", "resourceUrl", this.queryTermPattern);
        verify(this.contentResult);
    }

    @Test
    public void testCompareToDescriptionHits() {
        this.queryTermPattern = QueryTermPattern.getPattern("foo");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("bar1");
        this.contentResult1.setDescription("bar");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("bar2");
        this.contentResult2.setDescription("foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
        // title hits and description hits
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("title foo bar1");
        this.contentResult1.setDescription("just bar");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("title foo bar2");
        this.contentResult2.setDescription("i contain foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
    }

    @Test
    public void testCompareToExactTitle() {
        this.queryTermPattern = QueryTermPattern.getPattern("exact title match");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("exact title match");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) < 0);
    }

    @Test
    public void testCompareToNonFiling() {
        this.queryTermPattern = QueryTermPattern.getPattern("q");
        this.contentResult1 = new DefaultContentResult("foo");
        this.contentResult1.setTitle("a title");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("foo");
        this.contentResult2.setTitle("title");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
        this.queryTermPattern = QueryTermPattern.getPattern("q");
        this.contentResult1 = new DefaultContentResult("foo");
        this.contentResult1.setTitle("an title");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("foo");
        this.contentResult2.setTitle("title");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
        this.queryTermPattern = QueryTermPattern.getPattern("q");
        this.contentResult1 = new DefaultContentResult("foo");
        this.contentResult1.setTitle("the title");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("foo");
        this.contentResult2.setTitle("title");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
    }

    @Test
    public void testCompareToTitleBeginsWith() {
        this.queryTermPattern = QueryTermPattern.getPattern("title begins with");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("not title begins with");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("title begins with yes");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
    }

    @Test
    public void testCompareToWeighting() {
        this.queryTermPattern = QueryTermPattern.getPattern("query terms");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        // double weight
        this.contentResult2 = new DefaultContentResult("pubmed_cochrane_reviews");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
        this.contentResult1 = new DefaultContentResult("xxxx");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        // half weight
        this.contentResult2 = new DefaultContentResult("pubmed_recent_reviews");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) < 0);
        this.contentResult1 = new DefaultContentResult("xxxx");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        // quarter weight
        this.contentResult2 = new DefaultContentResult("medlineplus_0");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) < 0);
        this.contentResult1 = new DefaultContentResult("xxxx");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, null, null, null, null, this.queryTermPattern);
        // equal weight, equal title
        this.contentResult2 = new DefaultContentResult("yyyy");
        this.contentResult2.setTitle("foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, null, null, null, null, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
    }

    @Test
    public void testEqualsObject() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("the title").times(4);
        expect(result.getId()).andReturn("id");
        expect(result.getDescription()).andReturn("description").times(2);
        expect(result.getPublicationDate()).andReturn("publicationDate").times(2);
        replay(result);
        ContentResultSearchResult other = new ContentResultSearchResult(result, null, null, null, null, this.queryTermPattern);
        assertEquals(this.result, other);
        verify(result);
    }

    @Test
    public void testGetScore() {
        assertEquals(1, this.result.getScore());
    }

    @Test
    public void testGetSortTitle() {
        assertEquals("title", this.result.getSortTitle());
    }

    @Test
    public void testHashCode() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("the title").times(4);
        expect(result.getId()).andReturn("id");
        expect(result.getDescription()).andReturn("description").times(2);
        expect(result.getPublicationDate()).andReturn("publicationDate").times(2);
        replay(result);
        ContentResultSearchResult other = new ContentResultSearchResult(result, null, null, null, null, this.queryTermPattern);
        assertEquals(this.result.hashCode(), other.hashCode());
        verify(result);
    }

    @Test
    public void testGetResourceHits() {
        assertEquals("resourceHits", this.result.getResourceHits());
    }

    @Test
    public void testGetResourceId() {
        assertEquals("resourceId", this.result.getResourceId());
    }

    @Test
    public void testGetResourceName() {
        assertEquals("resourceName", this.result.getResourceName());
    }

    @Test
    public void testGetResourceUrl() {
        assertEquals("resourceUrl", this.result.getResourceUrl());
    }
/*
    @Test
    public void testToSAX() throws SAXException {
        XMLConsumer xmlConsumer = createMock(XMLConsumer.class);
        reset(this.contentResult);
        xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(12);
        expect(this.contentResult.getId()).andReturn("id");
        xmlConsumer.characters(isA(char[].class), eq(0), gt(0));
        expectLastCall().times(11);
        xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(12);
        expect(this.contentResult.getContentId()).andReturn("contentId");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getDescription()).andReturn("description");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationDate()).andReturn("publicationDate");
        expect(this.contentResult.getPublicationTitle()).andReturn("publicationTitle");
        expect(this.contentResult.getPublicationVolume()).andReturn("publicationVolume");
        expect(this.contentResult.getPublicationIssue()).andReturn("publicationIssue");
        expect(this.contentResult.getPages()).andReturn("pages");
        expect(this.contentResult.getURL()).andReturn("url");
        replay(this.contentResult, xmlConsumer);
        this.result.toSAX(xmlConsumer);
        verify(this.contentResult, xmlConsumer);
    }
    */
}
