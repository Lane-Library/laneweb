package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        Map<Result, Collection<Result>> resultContentMap = getContentResultMap(result);
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().getSearchText());
        for (Entry<Result, Collection<Result>> entry : resultContentMap.entrySet()) {
            Result hitCount = entry.getKey();
            for (Result c : entry.getValue()) {
                ContentResult contentResult = (ContentResult) c;
                // create a ContentResultSearchResult from each ContentResult, retain the highest scoring one if more
                // than
                // one the same
                int score = this.scoreStrategy.computeScore(contentResult, queryTermPattern);
                ContentResultSearchResult current = new ContentResultSearchResult(contentResult, hitCount, score);
                ContentResultSearchResult previous = resultMap.get(current);
                if (previous == null || current.getScore() > previous.getScore()) {
                    resultMap.put(current, current);
                }
            }
        }
        this.scopusDeduplicator.removeDuplicates(resultMap.values());
        return new LinkedList<SearchResult>(resultMap.values());
    }

    /**
     * creates a Map where the keys are hit count Results and the values are the associated Collection
     * of ContentResults.
     * @param result the containing metasearch Result
     * @return the Map as noted above.
     */
    private Map<Result, Collection<Result>> getContentResultMap(final Result result) {
        Map<Result, Collection<Result>> resultMap = new HashMap<Result, Collection<Result>>();
        Collection<Result> engines;
        synchronized(result) {
            engines = result.getChildren();
        }
        for (Result engine : engines) {
            Collection<Result> children;
            synchronized(engine) {
                children = engine.getChildren();
            }
            Result hitCount = null;
            Collection<Result> contents = Collections.emptySet();
            for (Result child : children) {
                if (child.getId().endsWith(UNDERSCORE_CONTENT)) {
                    contents = child.getChildren();
                } else {
                    hitCount = child;
                }
            }
            resultMap.put(hitCount, contents);
        }
        return resultMap;
    }
}