/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

import edu.stanford.irt.search.impl.DefaultContentResult;

/**
 * @author ryanmax
 *
 */
public class ContentResultSearchResultTest {

    Pattern queryTermPattern;

    DefaultContentResult contentResult1;

    DefaultContentResult contentResult2;

    ContentResultSearchResult contentResultSearchResult1;

    ContentResultSearchResult contentResultSearchResult2;

    @Test
    public void testCompareToNonFiling() {
        this.queryTermPattern = QueryTermPattern.getPattern("q");
        this.contentResult1 = new DefaultContentResult("foo");
        this.contentResult1.setTitle("a title");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("foo");
        this.contentResult2.setTitle("title");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
        
        this.queryTermPattern = QueryTermPattern.getPattern("q");
        this.contentResult1 = new DefaultContentResult("foo");
        this.contentResult1.setTitle("an title");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("foo");
        this.contentResult2.setTitle("title");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
        
        this.queryTermPattern = QueryTermPattern.getPattern("q");
        this.contentResult1 = new DefaultContentResult("foo");
        this.contentResult1.setTitle("the title");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("foo");
        this.contentResult2.setTitle("title");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
    }

    @Test
    public void testCompareToExactTitle() {
        this.queryTermPattern = QueryTermPattern.getPattern("exact title match");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("exact title match");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) < 0);
    }

    @Test
    public void testCompareToTitleBeginsWith() {
        this.queryTermPattern = QueryTermPattern.getPattern("title begins with");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("not title begins with");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("title begins with yes");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
    }

    @Test
    public void testCompareToDescriptionHits() {
        this.queryTermPattern = QueryTermPattern.getPattern("foo");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("bar1");
        this.contentResult1.setDescription("bar");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("bar2");
        this.contentResult2.setDescription("foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
        
        // title hits and description hits
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("title foo bar1");
        this.contentResult1.setDescription("just bar");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        this.contentResult2 = new DefaultContentResult("pubmed");
        this.contentResult2.setTitle("title foo bar2");
        this.contentResult2.setDescription("i contain foo");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
    }

    @Test
    public void testCompareToWeighting() {
        this.queryTermPattern = QueryTermPattern.getPattern("query terms");
        this.contentResult1 = new DefaultContentResult("pubmed");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        // double weight
        this.contentResult2 = new DefaultContentResult("pubmed_cochrane_reviews");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) > 0);
        
        this.contentResult1 = new DefaultContentResult("xxxx");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        // half weight
        this.contentResult2 = new DefaultContentResult("pubmed_recent_reviews");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) < 0);
        
        this.contentResult1 = new DefaultContentResult("xxxx");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        // quarter weight
        this.contentResult2 = new DefaultContentResult("medlineplus_0");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) < 0);
        
        this.contentResult1 = new DefaultContentResult("xxxx");
        this.contentResult1.setTitle("foo");
        this.contentResultSearchResult1 = new ContentResultSearchResult(this.contentResult1, this.queryTermPattern);
        // equal weight
        this.contentResult2 = new DefaultContentResult("yyyy");
        this.contentResult2.setTitle("foo bar");
        this.contentResultSearchResult2 = new ContentResultSearchResult(this.contentResult2, this.queryTermPattern);
        assertTrue(this.contentResultSearchResult1.compareTo(this.contentResultSearchResult2) == 0);
    }
}
