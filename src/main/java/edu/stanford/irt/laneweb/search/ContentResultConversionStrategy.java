package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;


public class ContentResultConversionStrategy {

    private static final int CONTENT_RESULT_LIMIT = 20;

    private static final String UNDERSCORE_CONTENT = "_content";
    
    private ScoreStrategy scoreStrategy;
    
    public ContentResultConversionStrategy(ScoreStrategy scoreStrategy) {
        this.scoreStrategy = scoreStrategy;
    }

    public Collection<SearchResult> convertResult(final Result result) {
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().toString());
        Collection<SearchResult> contentResults = new LinkedList<SearchResult>();
        Map<String, ContentResultSearchResult> resultTitles = new HashMap<String, ContentResultSearchResult>();
        for (Result engine : result.getChildren()) {
            Result parentResource = null;
            for (Result resource : engine.getChildren()) {
                String resourceId = resource.getId();
                if (resourceId.endsWith(UNDERSCORE_CONTENT)) {
                    Iterator<Result> it = resource.getChildren().iterator();
                    int count = 0;
                    while (it.hasNext() && count < CONTENT_RESULT_LIMIT) {
                        ContentResult contentResult = (ContentResult) it.next();
                        count++;
                        int score = this.scoreStrategy.computeScore(contentResult, queryTermPattern);
                        ContentResultSearchResult crsr = new ContentResultSearchResult(contentResult, parentResource, score);
                        String contentId = contentResult.getContentId();
                        String crsrKey = contentId != null ? contentId : contentResult.getURL();
                        if (!resultTitles.containsKey(crsrKey)) {
                            resultTitles.put(crsrKey, crsr);
                            contentResults.add(crsr);
                        } else if (score > resultTitles.get(crsrKey).getScore()) {
                            contentResults.remove(resultTitles.get(crsrKey));
                            contentResults.add(crsr);
                            resultTitles.remove(crsrKey);
                            resultTitles.put(crsrKey, crsr);
                        }
                    }
                } else {
                    parentResource = resource;
                }
            }
        }
        return contentResults;
    }
}
/*
 * protected Collection<ContentResultSearchResult> getContentResultList(final
 * Result result) { Map<String, ContentResultSearchResult> results = new
 * HashMap<String, ContentResultSearchResult>(); Pattern queryTermPattern =
 * QueryTermPattern.getPattern(this.query); for (Result engine :
 * result.getChildren()) { Result resource = null; Result contents = null; for
 * (Result child : engine.getChildren()) { String resourceId = child.getId(); if
 * (resourceId.endsWith(UNDERSCORE_CONTENT)) { contents = child; } else {
 * resource = child; } } if (resource != null && contents != null) { String
 * resourceHits = resource.getHits(); String resourceId = resource.getId();
 * String resourceName = resource.getDescription(); String resourceUrl =
 * resource.getURL(); Iterator<Result> it = contents.getChildren().iterator();
 * int count = 0; while (it.hasNext() && count < CONTENT_RESULT_LIMIT) {
 * count++; ContentResult content = (ContentResult) it.next();
 * ContentResultSearchResult searchResult = new
 * ContentResultSearchResult(content, resourceHits, resourceId, resourceName,
 * resourceUrl, queryTermPattern); String id = content.getContentId(); String
 * key = id == null ? content.getURL() : id; SearchResult stored =
 * results.get(key); if (stored == null || searchResult.getScore() >
 * stored.getScore()) { results.put(key, searchResult); } } } } return
 * results.values(); }
 */