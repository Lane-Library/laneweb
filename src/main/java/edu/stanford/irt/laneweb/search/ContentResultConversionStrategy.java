package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ContentResultConversionStrategy {

    private static final String UNDERSCORE_CONTENT = "_content";

    private ScopusDeduplicator scopusDeduplicator = new ScopusDeduplicator();

    private ScoreStrategy scoreStrategy = new ScoreStrategy();

    public ContentResultConversionStrategy() {
    }

    // constructor for unit tests
    ContentResultConversionStrategy(final ScoreStrategy scoreStrategy, final ScopusDeduplicator deduplicator) {
        this.scoreStrategy = scoreStrategy;
        this.scopusDeduplicator = deduplicator;
    }

    public List<SearchResult> convertResult(final Result result) {
        Map<ContentResultSearchResult, ContentResultSearchResult> resultMap = new HashMap<ContentResultSearchResult, ContentResultSearchResult>();
        List<ContentResult> contentResults = getContentResultList(result);
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().toString());
        for (ContentResult contentResult : contentResults) {
            // create a ContentResultSearch result from each ContentResult, retain the highest scoring one if more than
            // one the same
            int score = this.scoreStrategy.computeScore(contentResult, queryTermPattern);
            ContentResultSearchResult current = new ContentResultSearchResult(contentResult, contentResult.getParent(),
                    score);
            ContentResultSearchResult previous = resultMap.get(current);
            if (previous == null || current.getScore() > previous.getScore()) {
                resultMap.put(current, current);
            }
        }
        this.scopusDeduplicator.removeDuplicates(resultMap.values());
        return new LinkedList<SearchResult>(resultMap.values());
    }

    // extracts a list of ContentResults from the initial Result object
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