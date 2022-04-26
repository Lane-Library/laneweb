package edu.stanford.irt.laneweb.eresources.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.search.AbstractSearchGenerator;

public class SolrSearchGenerator extends AbstractSearchGenerator<SolrSearchResult> {

    private static final int DEFAULT_RESULTS = 20;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private String facets;

    private Integer pageNumber = Integer.valueOf(0);

    private String searchTerm;

    private SolrService solrService;

    private String sort;

    private String type;

    public SolrSearchGenerator(final SolrService solrService, final SAXStrategy<SolrSearchResult> saxStrategy) {
        super(saxStrategy);
        this.solrService = solrService;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.facets = ModelUtil.getString(model, Model.FACETS);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.parseInt(page) - 1;
        }
        this.searchTerm = ModelUtil.getString(model, Model.QUERY);
        this.sort = ModelUtil.getString(model, Model.SORT, "");
        this.type = ModelUtil.getString(model, Model.TYPE);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            try {
                this.type = URLDecoder.decode(parameters.get(Model.TYPE), UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
    }

    private void highlightResults(final Page<Eresource> page) {
        if (null != page) {
            SolrResultPage<Eresource> solrPage = (SolrResultPage<Eresource>) page;
            if (!solrPage.getHighlighted().isEmpty()) {
                solrPage.getHighlighted().stream().forEach((final HighlightEntry<Eresource> hightlight) -> {
                    Eresource er = hightlight.getEntity();
                    hightlight.getHighlights().forEach((final Highlight h) -> {
                        String field = h.getField().getName();
                        String highlightedData = h.getSnipplets().get(0);
                        if ("title".equals(field)) {
                            er.setTitle(highlightedData);
                        } else if ("description".equals(field)) {
                            er.setDescription(highlightedData);
                        } else if ("publicationText".equals(field)) {
                            er.setPublicationText(highlightedData);
                        } else if ("publicationAuthorsText".equals(field)) {
                            er.setPublicationAuthorsText(highlightedData);
                        }
                    });
                });
            }
        }
    }

    private Sort parseSortParam() {
        List<Order> orders = new ArrayList<>();
        for (String string : this.sort.split(",")) {
            String[] s = string.split(" ");
            if (s.length == 2 && !s[0].isEmpty()) {
                orders.add(new Order(Direction.fromString(s[1]), s[0]));
            }
        }
        if (!orders.isEmpty()) {
            return Sort.by(orders);
        }
        return null;
    }

    @Override
    protected SolrSearchResult doSearch(final String query) {
        Sort sorts = parseSortParam();
        Pageable pageRequest = new SolrPageRequest(this.pageNumber.intValue(), DEFAULT_RESULTS, sorts);
        Page<Eresource> page;
        if (this.type == null) {
            page = this.solrService.searchWithFilters(query, this.facets, pageRequest);
        } else {
            page = this.solrService.searchType(this.type, this.searchTerm, pageRequest);
        }
        highlightResults(page);
        return new SolrSearchResult(query, page);
    }

    @Override
    protected SolrSearchResult getEmptyResult() {
        return SolrSearchResult.EMPTY_RESULT;
    }
}
