package edu.stanford.irt.laneweb.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.SolrCollectionManager;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class SolrSearchGenerator extends AbstractSearchGenerator<Map<String, Object>> implements ParametersAware {

    private static final int DEFAULT_RESULTS = 50;

    private SolrCollectionManager collectionManager;

    private Integer pageNumber = new Integer(0);

    private String searchTerm;

    private String type;

    public SolrSearchGenerator(final SolrCollectionManager collectionManager,
            final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(saxStrategy);
        this.collectionManager = collectionManager;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page) - 1;
        }
        this.searchTerm = ModelUtil.getString(model, Model.QUERY);
        this.type = ModelUtil.getString(model, Model.TYPE);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            try {
                this.type = URLDecoder.decode(parameters.get(Model.TYPE), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
    }

    @Override
    protected Map<String, Object> doSearch(final String query) {
        Map<String, Object> result = new HashMap<String, Object>();
        PageRequest pageRequest = new PageRequest(this.pageNumber.intValue(), DEFAULT_RESULTS);
        if (null != this.type) {
            result.put("resultPage", this.collectionManager.searchType(this.type, this.searchTerm, pageRequest));
        } else {
            result.put("resultPage", this.collectionManager.search(this.searchTerm, pageRequest));
        }
        result.put("searchTerm", this.searchTerm);
        return result;
    }
}
