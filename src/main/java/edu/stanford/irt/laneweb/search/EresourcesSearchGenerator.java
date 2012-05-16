package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.ParametersAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class EresourcesSearchGenerator extends AbstractSearchGenerator implements ParametersAware {

    private CollectionManager collectionManager;

    private String type;

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
    }

    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        PagingXMLizableSearchResultSet eresources = new PagingXMLizableSearchResultSet(this.query, this.page);
        eresources.addAll(getEresourceList());
        try {
            eresources.toSAX(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    protected Collection<SearchResult> getEresourceList() {
        if (null == this.query) {
            throw new IllegalStateException("null query");
        }
        Collection<Eresource> eresources = null;
        if (null != this.type) {
            eresources = this.collectionManager.searchType(this.type, this.query);
        } else {
            eresources = this.collectionManager.search(this.query);
        }
        Collection<SearchResult> results = new LinkedList<SearchResult>();
        for (Eresource eresource : eresources) {
            results.add(new EresourceSearchResult(eresource));
        }
        return results;
    }
}
