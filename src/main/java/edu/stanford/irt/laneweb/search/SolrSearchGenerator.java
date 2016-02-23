package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.solr.core.query.SolrPageRequest;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.solr.SolrService;

public class SolrSearchGenerator extends AbstractSearchGenerator<Map<String, Object>> {

    private static final int DEFAULT_RESULTS = 50;

    private SolrService solrService;

    private String facets;

    private Integer pageNumber = Integer.valueOf(0);

    private String sort;

    public SolrSearchGenerator(final SolrService solrService,
            final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(saxStrategy);
        this.solrService = solrService;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.facets = ModelUtil.getString(model, Model.FACETS);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page) - 1;
        }
        this.sort = ModelUtil.getString(model, Model.SORT, "");
    }

    @Override
    protected Map<String, Object> doSearch(final String query) {
        Map<String, Object> result = new HashMap<>();
        Sort sorts = parseSortParam();
        Pageable pageRequest = new SolrPageRequest(this.pageNumber.intValue(), DEFAULT_RESULTS, sorts);
        result.put("resultPage", this.solrService.searchWithFilters(query, this.facets, pageRequest));
        result.put("searchTerm", query);
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
