/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.searchresults.ContentResultSearchResult;
import edu.stanford.irt.laneweb.searchresults.XMLizableSearchResultsList;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class ContentSearchGenerator extends AbstractSearchGenerator {
    
    private static final String[] NO_ENGINES = new String[0];

    protected String[] engines;

    private long defaultTimeout;

    private int contentResultLimit;

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        super.setup(resolver, objectModel, src, par);
        this.engines = getObject("engines", String[].class, NO_ENGINES);
    }

    @Override
    public void generate() throws SAXException {
        XMLizableSearchResultsList mergedSearchResults = new XMLizableSearchResultsList();
        mergedSearchResults.setQuery(this.query);
        mergedSearchResults.setContentResultSearchResults(getContentResultList());
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
        Collection<String> engns = new HashSet<String>();
        Collection<ContentResultSearchResult> contentResults = new LinkedList<ContentResultSearchResult>();
        if ((this.engines != null) && (this.engines.length > 0)) {
            for (String element : this.engines) {
                engns.add(element);
            }
        }
        final SimpleQuery query = new SimpleQuery(this.query);
        Result result = this.metaSearchManager.search(query, this.defaultTimeout, engns, true);
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
