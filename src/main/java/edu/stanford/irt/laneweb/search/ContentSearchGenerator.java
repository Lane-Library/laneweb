/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax $Id$
 */
public class ContentSearchGenerator extends AbstractMetasearchGenerator {

    private int contentResultLimit;

    private long defaultTimeout;

    private String timeout;

    protected Collection<String> engines;

    @Override
    public void generate() throws SAXException {
        XMLizableSearchResultSet mergedSearchResults = new XMLizableSearchResultSet(this.query);
        mergedSearchResults.addAll(getContentResultList(doSearch()));
        this.xmlConsumer.startDocument();
        mergedSearchResults.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setContentResultLimit(final int contentResultLimit) {
        this.contentResultLimit = contentResultLimit;
    }

    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    protected Result doSearch() {
        long time = this.defaultTimeout;
        if (null != this.timeout) {
            try {
                time = Long.parseLong(this.timeout);
            } catch (NumberFormatException nfe) {
                ;
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
                if (resourceId.matches(".*_content")) {
                    Iterator<Result> it = resource.getChildren().iterator();
                    int count = 0;
                    while (it.hasNext() && count < this.contentResultLimit) {
                        count++;
                        ContentResultSearchResult crsr = new ContentResultSearchResult((ContentResult) it.next(),
                                queryTermPattern);
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
                } else{
                    parentResource = resource;
                }
            }
        }
        return contentResults;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initialize() {
        super.initialize();
        this.timeout = this.model.getString("timeout", this.parameterMap.get("timeout"));
        this.engines = this.model.getObject(Model.ENGINES, Collection.class, Collections.<String> emptyList());
        if (this.engines.size() == 0) {
            String engineList = this.parameterMap.get("engine-list");
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }
}
