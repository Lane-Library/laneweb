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

    private Map<Result, Collection<Result>> getContentResultMap(final Result result) {
        Map<Result, Collection<Result>> resultMap = new HashMap<Result, Collection<Result>>();
        for (Result engine : result.getChildren()) {
            // TODO: operations on engine should be synchronized
            Result hitCount = null;
            Collection<Result> contents = Collections.emptySet();
            for (Result child : engine.getChildren()) {
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