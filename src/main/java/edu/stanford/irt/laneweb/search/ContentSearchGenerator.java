/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.search.MetaSearchManagerSource;
import edu.stanford.irt.laneweb.searchresults.ContentResultSearchResult;
import edu.stanford.irt.laneweb.searchresults.XMLizableSearchResultsList;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class ContentSearchGenerator implements Generator {

    protected String query;

    protected String[] engines;

    private long defaultTimeout;

    private int contentResultLimit;

    private XMLConsumer xmlConsumer;

    protected MetaSearchManager metaSearchManager;

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = par.getParameter("query", null);
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        this.engines = request.getParameterValues("e");
        if (null != this.query && this.query.length() == 0) {
            throw new IllegalStateException("null query");
        }
        if (null == this.engines) {
            throw new IllegalArgumentException("null engines");
        }
    }

    public void generate() throws SAXException {
        XMLizableSearchResultsList mergedSearchResults = new XMLizableSearchResultsList();
        mergedSearchResults.setContentResultSearchResults(getContentResultList());
        this.xmlConsumer.startDocument();
        mergedSearchResults.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        if (null == msms) {
            throw new IllegalStateException("null metaSearchManager");
        }
        this.metaSearchManager = msms.getMetaSearchManager();
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
                        crsr.setQueryTermPattern(this.query);
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
