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

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.Initializable;
import edu.stanford.irt.laneweb.cocoon.ParametersAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 */
public class ContentSearchGenerator extends AbstractMetasearchGenerator implements ParametersAware, Initializable {

    private static final Pattern CONTENT_PATTERN = Pattern.compile(".*_content");

    protected Collection<String> engines;

    private int contentResultLimit;

    private long defaultTimeout;

    private Map<String, Object> model;

    private Map<String, String> parameters;

    private String timeout;

    @SuppressWarnings("unchecked")
    public void initialize() {
        this.timeout = ModelUtil.getString(this.model, Model.TIMEOUT, this.parameters.get(Model.TIMEOUT));
        this.engines = ModelUtil.getObject(this.model, Model.ENGINES, Collection.class, Collections.<String> emptyList());
        if (this.engines.size() == 0) {
            String engineList = this.parameters.get(Model.ENGINES);
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }

    public void setContentResultLimit(final int contentResultLimit) {
        this.contentResultLimit = contentResultLimit;
    }

    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.model = model;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
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
        long time = this.defaultTimeout;
        if (null != this.timeout) {
            try {
                time = Long.parseLong(this.timeout);
            } catch (NumberFormatException nfe) {
                time = this.defaultTimeout;
            }
        }
        return this.metaSearchManager.search(new SimpleQuery(this.query), time, this.engines, true);
    }

    protected Collection<ContentResultSearchResult> getContentResultList(final Result result) {
        Collection<ContentResultSearchResult> contentResults = new LinkedList<ContentResultSearchResult>();
        Map<String, ContentResultSearchResult> resultTitles = new HashMap<String, ContentResultSearchResult>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(this.query);
        for (Result engine : result.getChildren()) {
            Result parentResource = null;
            for (Result resource : engine.getChildren()) {
                String resourceId = resource.getId();
                if (CONTENT_PATTERN.matcher(resourceId).matches()) {
                    Iterator<Result> it = resource.getChildren().iterator();
                    int count = 0;
                    while (it.hasNext() && count < this.contentResultLimit) {
                        count++;
                        ContentResultSearchResult crsr = new ContentResultSearchResult((ContentResult) it.next(), queryTermPattern);
                        crsr.setResourceHits(parentResource.getHits());
                        crsr.setResourceId(parentResource.getId());
                        crsr.setResourceName(parentResource.getDescription());
                        crsr.setResourceUrl(parentResource.getURL());
                        if (!resultTitles.containsKey(crsr.getContentUrl())) {
                            resultTitles.put(crsr.getContentUrl(), crsr);
                            contentResults.add(crsr);
                        } else if (crsr.getScore() > resultTitles.get(crsr.getContentUrl()).getScore()) {
                            contentResults.remove(resultTitles.get(crsr.getContentUrl()));
                            contentResults.add(crsr);
                            resultTitles.remove(crsr.getContentUrl());
                            resultTitles.put(crsr.getContentUrl(), crsr);
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
