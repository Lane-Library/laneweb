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

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.searchresults.ContentResultSearchResult;
import edu.stanford.irt.laneweb.searchresults.EresourceSearchResult;
import edu.stanford.irt.laneweb.searchresults.XMLizableSearchResultSet;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class MergedSearchGenerator extends AbstractSearchGenerator {

    protected Collection<String> engines;

    private long defaultMetasearchTimeout;
    
    private int contentResultLimit;

    protected CollectionManager collectionManager;

    private MetaSearchManager metaSearchManager;

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

    public void generate() throws SAXException {
        XMLizableSearchResultSet mergedSearchResults = new XMLizableSearchResultSet(this.query);
        mergedSearchResults.addAll(getEresourceList());
        mergedSearchResults.addAll(getContentResultList());
        this.xmlConsumer.startDocument();
        mergedSearchResults.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    public void setDefaultMetasearchTimeout(final long defaultTimeout) {
        this.defaultMetasearchTimeout = defaultTimeout;
    }

    public void setContentResultLimit(final int contentResultLimit) {
        this.contentResultLimit = contentResultLimit;
    }
    
    private Collection<EresourceSearchResult> getEresourceList() {
        Collection<EresourceSearchResult> eresourceResults = new LinkedList<EresourceSearchResult>();
        for (Eresource eresource : this.collectionManager.search(this.query)) {
            eresourceResults.add(new EresourceSearchResult(eresource));
        }
        return eresourceResults;
    }

    private Collection<ContentResultSearchResult> getContentResultList() {
        Collection<ContentResultSearchResult> contentResults = new LinkedList<ContentResultSearchResult>();
        Pattern queryTermPattern = QueryTermPattern.getPattern(this.query);
        final SimpleQuery query = new SimpleQuery(this.query);
        Result result = this.metaSearchManager.search(query, this.defaultMetasearchTimeout, this.engines, true);
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
}
