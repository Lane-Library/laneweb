package edu.stanford.irt.laneweb.eresources.search;

import static org.springframework.data.domain.PageRequest.of;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.HighlightEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.HighlightEntry.Highlight;
import edu.stanford.irt.laneweb.eresources.model.solr.RestPage;
import edu.stanford.irt.laneweb.eresources.model.solr.RestResult;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.search.AbstractSearchGenerator;

public class SolrSearchGenerator extends AbstractSearchGenerator<RestResult<Eresource>> {

    private static final int DEFAULT_RESULTS = 20;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private String facets;

    private Integer pageNumber = Integer.valueOf(0);

    private String searchTerm;

    private EresourceSearchService searchService;

    private String sort;

    private String type;

    public SolrSearchGenerator(final EresourceSearchService searchService, final SAXStrategy<RestResult<Eresource>> saxStrategy) {
        super(saxStrategy);
        this.searchService = searchService;
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

    private void checkForExactMatch(final Page<Eresource> page) {
        Collection<Eresource> eresources = page.getContent();
        for (Eresource eresource : eresources) {
            String title = eresource.getTitle();
            if (null != title) {
                title = title.replace("___", "").replace(":::", "");
                if (this.searchTerm.equalsIgnoreCase(title)) {
                    eresource.setAnExactMatch(true);
                    break;
                }
            }
        }
    }

    
    

    private Page<Eresource> highlightPage(final Page<Eresource> page) {
        List<Eresource> content = new ArrayList<>();
        if (null != page) {
            RestPage<Eresource> solrPage = (RestPage<Eresource>) page;
            if (!solrPage.getHighlighted().isEmpty()) {
                solrPage.getHighlighted().stream().forEach((final HighlightEntry<Eresource> hightlight) -> {
                    Eresource er = hightlight.getEntity();
                    content.add(er);
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
       
        PageRequest pageRequest = PageRequest.of(page.getNumber(), page.getSize());  
        return new RestPage<>(content, pageRequest, page.getTotalElements() );
        } 
        return null;
    }


    private Pageable getPageRequest() {
        List<Order> orders = new ArrayList<>();
        for (String string : this.sort.split(",")) {
            String[] s = string.split(" ");
            if (s.length == 2 && !s[0].isEmpty()) {
                orders.add(new Order(Direction.fromString(s[1]), s[0]));
            }
        }
        if (!orders.isEmpty()) {
            return of(this.pageNumber.intValue(), DEFAULT_RESULTS, Sort.by(orders));
        }
        return of(this.pageNumber.intValue(), DEFAULT_RESULTS);
    }

    @Override
    protected RestResult<Eresource> doSearch(final String query) {
        Pageable pageRequest = this.getPageRequest();
        Page<Eresource> page;
        if (this.type == null) {
            page = this.searchService.searchWithFilters(query, this.facets, pageRequest);
        } else {
            page = this.searchService.searchType(this.type, this.searchTerm, pageRequest);
        }
        page = highlightPage(page);
        if (this.pageNumber == 0) {
            checkForExactMatch(page);
        }
        return new RestResult<Eresource>(query, page);
    }

    @Override
    protected RestResult<Eresource> getEmptyResult() {
        return RestResult.EMPTY_RESULT;
    }
}
