package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ContentResultConversionStrategy {

    private ScopusDeduplicator scopusDeduplicator = new ScopusDeduplicator();

    private ScoreStrategy scoreStrategy = new ScoreStrategy();

    public ContentResultConversionStrategy() {
        // default empty constructor
    }

    // constructor for unit tests
    ContentResultConversionStrategy(final ScoreStrategy scoreStrategy, final ScopusDeduplicator deduplicator) {
        this.scoreStrategy = scoreStrategy;
        this.scopusDeduplicator = deduplicator;
    }

    public List<SearchResult> convertResult(final Result result) {
        Map<ContentResultSearchResult, ContentResultSearchResult> resultMap = new HashMap<>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().getSearchText());
        Collection<Result> engines;
        synchronized (result) {
            engines = result.getChildren();
        }
        for (Result engine : engines) {
            processEngine(engine, resultMap, queryTermPattern);
        }
        this.scopusDeduplicator.removeDuplicates(resultMap.values());
        return new ArrayList<>(resultMap.values());
    }

    private void processEngine(final Result engine,
            final Map<ContentResultSearchResult, ContentResultSearchResult> resultMap, final Pattern queryTermPattern) {
        Collection<Result> children;
        synchronized (engine) {
            children = engine.getChildren();
        }
        for (Result resource : children) {
            processResource(resource, resultMap, queryTermPattern);
        }
    }

    private void processResource(final Result resource,
            final Map<ContentResultSearchResult, ContentResultSearchResult> resultMap, final Pattern queryTermPattern) {
        for (Result content : resource.getChildren()) {
            ContentResult contentResult = (ContentResult) content;
            // create a ContentResultSearchResult from each ContentResult, retain the highest scoring one if more
            // than one the same
            int score = this.scoreStrategy.computeScore(contentResult, queryTermPattern);
            ContentResultSearchResult current = new ContentResultSearchResult(contentResult, resource, score);
            ContentResultSearchResult previous = resultMap.get(current);
            if (previous == null || current.getScore() > previous.getScore()) {
                resultMap.put(current, current);
            }
        }
    }
}