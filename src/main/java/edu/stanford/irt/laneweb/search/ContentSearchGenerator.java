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
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.laneweb.LanewebException;
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

    private String timeout;

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
        PagingXMLizableSearchResultSet mergedSearchResults = new PagingXMLizableSearchResultSet(this.query, this.page);
        mergedSearchResults.addAll(getContentResultList(doSearch()));
        try {
            mergedSearchResults.toSAX(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
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
        if (this.query == null) {
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
                        count++;
                        ContentResultSearchResult crsr = new ContentResultSearchResult((ContentResult) it.next(), queryTermPattern);
                        String crsrKey = (crsr.getContentId() != null) ? crsr.getContentId() : crsr.getContentUrl();
                        crsr.setResourceHits(parentResource.getHits());
                        crsr.setResourceId(parentResource.getId());
                        crsr.setResourceName(parentResource.getDescription());
                        crsr.setResourceUrl(parentResource.getURL());
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
