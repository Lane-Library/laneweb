package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ContentResultConversionStrategy {

    private ScoreStrategy scoreStrategy = new ScoreStrategy();

    public ContentResultConversionStrategy() {
        // default empty constructor
    }

    // constructor for unit tests
    ContentResultConversionStrategy(final ScoreStrategy scoreStrategy) {
        this.scoreStrategy = scoreStrategy;
    }

    public List<SearchResult> convertResult(final Result result) {
        Map<SearchResult, SearchResult> resultMap = new HashMap<>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().getSearchText());
        Collection<Result> engines;
        synchronized (result) {
            engines = result.getChildren();
        }
        for (Result engine : engines) {
            processEngine(engine, resultMap, queryTermPattern);
        }
        List<SearchResult> results = new ArrayList<>(resultMap.values());
        Collections.sort(results);
        return results;
    }

    public List<SearchResult> convertResults(final Collection<Result> results, final String query) {
        Map<SearchResult, SearchResult> resultMap = new HashMap<>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(query);
        for (Result result : results) {
            processResource(result, resultMap, queryTermPattern);
        }
        return new ArrayList<>(resultMap.values());
    }

    private void processEngine(final Result engine, final Map<SearchResult, SearchResult> resultMap,
            final Pattern queryTermPattern) {
        Collection<Result> children;
        synchronized (engine) {
            children = engine.getChildren();
        }
        for (Result resource : children) {
            processResource(resource, resultMap, queryTermPattern);
        }
    }

    private void processResource(final Result resource, final Map<SearchResult, SearchResult> resultMap,
            final Pattern queryTermPattern) {
        for (Result content : resource.getChildren()) {
            ContentResult contentResult = (ContentResult) content;
            // create a SearchResult from each ContentResult, retain the highest scoring one if more
            // than one the same
            int score = this.scoreStrategy.computeScore(contentResult, queryTermPattern);
            SearchResult current = new SearchResult(contentResult, resource, score);
            SearchResult previous = resultMap.get(current);
            if (previous == null || current.getScore() > previous.getScore()) {
                resultMap.put(current, current);
            }
        }
    }
}