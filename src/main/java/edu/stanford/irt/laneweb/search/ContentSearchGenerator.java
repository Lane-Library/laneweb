package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 */
public class ContentSearchGenerator extends AbstractMetasearchGenerator implements ParametersAware {

    private static final int CONTENT_RESULT_LIMIT = 20;

    private static final long DEFAULT_TIMEOUT = 20000;

    private static final String UNDERSCORE_CONTENT = "_content";

    protected Collection<String> engines;

    private SAXStrategy<PagingSearchResultSet> saxStrategy;

    private String timeout;

    public ContentSearchGenerator(final SAXStrategy<PagingSearchResultSet> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class, Collections.<String> emptyList());
    }

    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
        if (this.engines.size() == 0) {
            String engineList = parameters.get(Model.ENGINES);
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        PagingSearchResultSet contentSearchResults = new PagingSearchResultSet(this.query, this.page);
        contentSearchResults.addAll(getContentResultList(doSearch()));
        this.saxStrategy.toSAX(contentSearchResults, xmlConsumer);
    }

    @Override
    protected Result doSearch() {
        long time = DEFAULT_TIMEOUT;
        if (null != this.timeout) {
            try {
                time = Long.parseLong(this.timeout);
            } catch (NumberFormatException nfe) {
                time = DEFAULT_TIMEOUT;
            }
        }
        Result result = null;
        if (this.query == null || this.query.isEmpty()) {
            result = new DefaultResult("");
        } else {
            return this.metaSearchManager.search(new SimpleQuery(this.query), time, this.engines, true);
        }
        return result;
    }

    protected Collection<ContentResultSearchResult> getContentResultList(final Result result) {
        Collection<ContentResultSearchResult> contentResults = new LinkedList<ContentResultSearchResult>();
        Map<String, ContentResultSearchResult> resultTitles = new HashMap<String, ContentResultSearchResult>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(this.query);
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
                        ContentResultSearchResult crsr = new ContentResultSearchResult(contentResult, parentResource,
                                queryTermPattern);
                        String crsrKey = (contentResult.getContentId() != null) ? contentResult.getContentId() : contentResult
                                .getURL();
                        if (!resultTitles.containsKey(crsrKey)) {
                            resultTitles.put(crsrKey, crsr);
                            contentResults.add(crsr);
                        } else if (crsr.getScore() > resultTitles.get(crsrKey).getScore()) {
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
