package edu.stanford.irt.laneweb.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.solr.core.query.SolrPageRequest;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.solr.SolrSearchService;

public class SolrSearchGenerator extends AbstractSearchGenerator<Map<String, Object>> implements ParametersAware {

    private static final int DEFAULT_RESULTS = 50;

    private SolrSearchService collectionManager;

    private String facets;

    private Integer pageNumber = Integer.valueOf(0);

    private String searchTerm;

    private String sort;

    private String type;

    public SolrSearchGenerator(final SolrSearchService collectionManager,
            final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(saxStrategy);
        this.collectionManager = collectionManager;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.facets = ModelUtil.getString(model, Model.FACETS);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page) - 1;
        }
        this.searchTerm = ModelUtil.getString(model, Model.QUERY);
        this.sort = ModelUtil.getString(model, Model.SORT, "");
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
        Map<String, Object> result = new HashMap<>();
        Sort sorts = parseSortParam();
        Pageable pageRequest = new SolrPageRequest(this.pageNumber.intValue(), DEFAULT_RESULTS, sorts);
        if (null != this.type) {
            result.put("resultPage", this.collectionManager.searchType(this.type, this.searchTerm, pageRequest));
        } else {
            result.put("resultPage",
                    this.collectionManager.searchWithFilters(this.searchTerm, this.facets, pageRequest));
        }
        result.put("searchTerm", this.searchTerm);
        return result;
    }

    private Sort parseSortParam() {
        List<Order> orders = new ArrayList<>();
        for (String string : this.sort.split(",")) {
            String[] s = string.split(" ");
            if (s.length == 2 && !s[0].isEmpty()) {
                orders.add(new Order(Direction.fromStringOrNull(s[1]), s[0]));
            }
        }
        if (!orders.isEmpty()) {
            return new Sort(orders);
        }
        return null;
    }
}
