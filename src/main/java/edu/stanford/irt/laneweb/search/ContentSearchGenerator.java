/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.searchresults.ContentResultSearchResult;
import edu.stanford.irt.laneweb.searchresults.XMLizableSearchResultSet;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class ContentSearchGenerator extends AbstractMetasearchGenerator {

    protected Collection<String> engines;

    private long defaultTimeout;

    private int contentResultLimit;

    protected void initialize() {
        super.initialize();
        this.engines = this.model.getObject(LanewebObjectModel.ENGINES, Collection.class, Collections.<String>emptyList());
        if (this.engines.size() == 0) {
            String engineList = this.parameterMap.get("engine-list");
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList,","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }

    @Override
    public void generate() throws SAXException {
        XMLizableSearchResultSet mergedSearchResults = new XMLizableSearchResultSet(this.query);
        mergedSearchResults.addAll(getContentResultList());
        this.xmlConsumer.startDocument();
        mergedSearchResults.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setContentResultLimit(final int contentResultLimit) {
        this.contentResultLimit = contentResultLimit;
    }
    
    private Collection<ContentResultSearchResult> getContentResultList() {
        Collection<ContentResultSearchResult> contentResults = new LinkedList<ContentResultSearchResult>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(this.query);
        final SimpleQuery query = new SimpleQuery(this.query);
        Result result = this.metaSearchManager.search(query, this.defaultTimeout, this.engines, true);
        for (Result engine : result.getChildren()) {
            Result parentResource = null;
            for (Result resource : engine.getChildren()) {
                String resourceId = resource.getId();
                if (resourceId.matches(".*_content")) {
                    Iterator<Result> it = resource.getChildren().iterator();
                    int count = 0;
                    while (it.hasNext() && count <= this.contentResultLimit){
                        count++;
                        ContentResultSearchResult crsr = new ContentResultSearchResult((ContentResult) it.next(), queryTermPattern);
                        crsr.setResourceHits(parentResource.getHits());
                        crsr.setResourceId(parentResource.getId());
                        crsr.setResourceName(parentResource.getDescription());
                        crsr.setResourceUrl(parentResource.getURL());
                        contentResults.add(crsr);
                    }
                } else if (!"article_ids".equals(resource.getId())) {
                    parentResource = resource;
                }
            }
        }
        return contentResults;
    }

    @Override
    protected Result doSearch() {
        throw new UnsupportedOperationException();
    }
}
