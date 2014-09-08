package edu.stanford.irt.laneweb.search;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.SolrCollectionManager;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class SolrSearchGenerator extends AbstractSearchGenerator<Map<String, Object>> {

    private static final int DEFAULT_RESULTS = 50;

    private SolrCollectionManager collectionManager;

    private Integer pageNumber = new Integer(0);

    private String searchTerm;

    public SolrSearchGenerator(final SolrCollectionManager collectionManager,
            final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(saxStrategy);
        this.collectionManager = collectionManager;
    }

    @Override
    protected Map<String, Object> doSearch(final String query) {
        Map<String, Object> result = new HashMap<String, Object>();
        PageRequest pageRequest = new PageRequest(this.pageNumber.intValue(), DEFAULT_RESULTS);
        result.put("resultPage", this.collectionManager.search(this.searchTerm, pageRequest));
        result.put("searchTerm", this.searchTerm);
        return result;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page);
        }
        this.searchTerm = ModelUtil.getString(model, Model.QUERY);
    }
}
