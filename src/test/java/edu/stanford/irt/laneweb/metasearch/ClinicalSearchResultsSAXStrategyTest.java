package edu.stanford.irt.laneweb.metasearch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsSAXStrategyTest {

    private ContentResult contentResult;

    private Result result;

    private ClinicalSearchResults results;

    private SearchResult searchResult;

    private ClinicalSearchResultsSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.strategy = new ClinicalSearchResultsSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.contentResult = ContentResult.newContentResultBuilder().id("id").title("title").build();
        this.result = Result.newResultBuilder().id("id").hits("10").url("url")
                .children(Collections.singletonList(this.contentResult)).build();
        List<Result> resourceResults = Collections.singletonList(this.result);
        this.searchResult = new SearchResult(this.contentResult, this.result, 0);
        List<SearchResult> results = Collections.singletonList(this.searchResult);
        PagingList<SearchResult> searchResults = new PagingList<>(results,
                new PagingData(results, 0, "", 50, Integer.MAX_VALUE));
        this.results = new ClinicalSearchResults(resourceResults, searchResults, results.size());
    }

    @Test
    public void testToSAX() throws IOException {
        this.strategy.toSAX(this.results, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ClinicalSearchResultsSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }
}
