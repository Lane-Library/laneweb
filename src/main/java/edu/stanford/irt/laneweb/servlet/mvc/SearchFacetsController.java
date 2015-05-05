package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.solr.SolrRepository;

@Controller
public class SearchFacetsController {

    private static final String FACETS = "facets";

    SolrRepository repository;

    SolrTemplate solrTemplate;

    @Autowired
    public SearchFacetsController(final SolrTemplate laneSearchSolrTemplate) {
        this.solrTemplate = laneSearchSolrTemplate;
        this.repository = new SolrRepositoryFactory(laneSearchSolrTemplate).getRepository(SolrRepository.class);
    }

    @RequestMapping(value = "/apps/search/facet")
    @ResponseBody
    public Map<String, Map<String, Map<String, Long>>> facetByField(final Model model,
            @RequestParam(value = "q", required = true) final String query,
            @RequestParam(value = "f", required = true) final String field, final HttpServletRequest request) {
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(query)).setFacetOptions(new FacetOptions()
                .addFacetOnField(field).setFacetMinCount(1).setFacetLimit(1000));
        fquery.setRequestHandler("/lane-facet");
        FacetPage<Eresource> fps = this.solrTemplate.queryForFacetPage(fquery, Eresource.class);
        return processFacets(fps);
    }

    @RequestMapping(value = "/apps/search/facets")
    @ResponseBody
    public Map<String, Map<String, Map<String, Long>>> facetByFields(final Model model,
            @RequestParam(value = "q", required = true) final String query, final HttpServletRequest request) {
        FacetPage<Eresource> fps = this.repository.facetByManyFields(query, new PageRequest(0, 1));
        return processFacets(fps);
    }

    private Map<String, Map<String, Map<String, Long>>> processFacets(final FacetPage<Eresource> facetpage) {
        Map<String, Map<String, Long>> facetsMap = new HashMap<String, Map<String, Long>>();
        Map<String, Long> countMap = new LinkedHashMap<String, Long>();
        long total = facetpage.getTotalElements();
        countMap.put("total", Long.valueOf(total));
        facetsMap.put("all", countMap);
        if (0 == total) {
            return Collections.singletonMap(FACETS, facetsMap);
        }
        for (Page<FacetFieldEntry> page : facetpage.getFacetResultPages()) {
            if (!page.hasContent()) {
                continue;
            }
            Map<String, Long> resultsMap = new LinkedHashMap<String, Long>();
            String fieldName = null;
            for (FacetFieldEntry entry : page) {
                if (fieldName == null) {
                    fieldName = entry.getField().getName();
                }
                String facetName = entry.getValue();
                Long facetValue = Long.valueOf(entry.getValueCount());
                resultsMap.put(facetName, facetValue);
            }
            facetsMap.put(fieldName, resultsMap);
        }
        return Collections.singletonMap(FACETS, facetsMap);
    }
}
