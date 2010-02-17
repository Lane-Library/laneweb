/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.search.AbstractSearchGenerator;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class MergedSearchGenerator extends AbstractSearchGenerator {
    
    private static final String[] NO_ENGINES  = new String[0];

    protected String[] engines;

    private long defaultMetasearchTimeout;
    
    private int contentResultLimit;

    protected CollectionManager collectionManager;

    public void initialize() {
        super.initialize();
        this.engines = this.model.getObject("engines", String[].class, NO_ENGINES);
    }

    public void generate() throws SAXException {
        XMLizableSearchResultsList mergedSearchResults = new XMLizableSearchResultsList();
        mergedSearchResults.setQuery(this.query);
        mergedSearchResults.setEresourceSearchResults(getEresourceList());
        mergedSearchResults.setContentResultSearchResults(getContentResultList());
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
        Collection<String> engns = new HashSet<String>();
        Collection<ContentResultSearchResult> contentResults = new LinkedList<ContentResultSearchResult>();
        if ((this.engines != null) && (this.engines.length > 0)) {
            for (String element : this.engines) {
                engns.add(element);
            }
        }
        final SimpleQuery query = new SimpleQuery(this.query);
        Result result = this.metaSearchManager.search(query, this.defaultMetasearchTimeout, engns, true);
        for (Result engine : result.getChildren()) {
            Result parentResource = null;
            for (Result resource : engine.getChildren()) {
                String resourceId = resource.getId();
                if (resourceId.matches(".*_content")) {
                    Iterator<Result> it = resource.getChildren().iterator();
                    int count = 0;
                    while (it.hasNext() && count <= this.contentResultLimit){
                        count++;
                        ContentResultSearchResult crsr = new ContentResultSearchResult((ContentResult) it.next());
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
