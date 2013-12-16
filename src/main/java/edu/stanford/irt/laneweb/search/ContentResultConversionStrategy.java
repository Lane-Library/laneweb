package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.DefaultContentResult;
import edu.stanford.irt.search.impl.DefaultResult;

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

    public List<SearchResult> convertResult(final DefaultResult result) {
        Map<ContentResultSearchResult, ContentResultSearchResult> resultMap = new HashMap<ContentResultSearchResult, ContentResultSearchResult>();
        List<DefaultContentResult> contentResults = getContentResultList(result);
        Pattern queryTermPattern = QueryTermPattern.getPattern(result.getQuery().toString());
        for (DefaultContentResult contentResult : contentResults) {
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
    private List<DefaultContentResult> getContentResultList(final DefaultResult result) {
        List<DefaultContentResult> list = new LinkedList<DefaultContentResult>();
        for (DefaultResult engine : result.getChildren()) {
            DefaultResult parent = null;
            Collection<DefaultResult> children = null;
            for (DefaultResult resource : engine.getChildren()) {
                String resourceId = resource.getId();
                if (resourceId.endsWith(UNDERSCORE_CONTENT)) {
                    children = resource.getChildren();
                } else {
                    parent = resource;
                }
            }
            if (children != null) {
                for (DefaultResult child : children) {
                    child.setParent(parent);
                    list.add((DefaultContentResult) child);
                }
            }
        }
        return list;
    }
}