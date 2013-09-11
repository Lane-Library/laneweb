package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ContentResultConversionStrategy {

    private static final String UNDERSCORE_CONTENT = "_content";

    private ScoreStrategy scoreStrategy = new ScoreStrategy();
    
    private ScopusDeduplicator deduplicator = new ScopusDeduplicator();
    
    public ContentResultConversionStrategy() {}

    // constructor for unit tests
    protected ContentResultConversionStrategy(final ScoreStrategy scoreStrategy, final ScopusDeduplicator deduplicator) {
        this.scoreStrategy = scoreStrategy;
        this.deduplicator = deduplicator;
    }

    public List<SearchResult> convertResult(final Result result) {
        Set<ContentResultSearchResult> searchResults = new HashSet<ContentResultSearchResult>();
        List<ContentResult> contentResults = getContentResultList(result);
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().toString());
        for (ContentResult contentResult : contentResults) {
            int score = this.scoreStrategy.computeScore(contentResult, queryTermPattern);
            searchResults.add(new ContentResultSearchResult(contentResult, contentResult.getParent(), score));
        }
        this.deduplicator.removeDuplicates(searchResults);
        return new LinkedList<SearchResult>(searchResults);
    }

    private List<ContentResult> getContentResultList(final Result result) {
        List<ContentResult> list = new LinkedList<ContentResult>();
        for (Result engine : result.getChildren()) {
            Result parent = null;
            Collection<Result> children = null;
            for (Result resource : engine.getChildren()) {
                String resourceId = resource.getId();
                if (resourceId.endsWith(UNDERSCORE_CONTENT)) {
                    children = resource.getChildren();
                } else {
                    parent = resource;
                }
            }
            if (children != null) {
                for (Result child : children) {
                    child.setParent(parent);
                    list.add((ContentResult) child);
                }
            }
        }
        return list;
    }
}