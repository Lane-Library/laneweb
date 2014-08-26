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
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.solr.SolrRepository;

@Controller
public class SearchFacetsController {

    private static final String FACETS = "facets";

    SolrRepository repository;

    @Autowired
    public SearchFacetsController(final SolrTemplate laneSearchSolrTemplate) {
        this.repository = new SolrRepositoryFactory(laneSearchSolrTemplate).getRepository(SolrRepository.class);
    }

    @RequestMapping(value = "/apps/search/facets")
    @ResponseBody
    public Map<String, Map<String, Map<String, Long>>> facetByField(final Model model,
            @RequestParam(value = "q", required = true) final String query, final HttpServletRequest request) {
        Map<String, Map<String, Long>> facetsMap = new HashMap<String, Map<String, Long>>();
        Map<String, Long> countMap = new LinkedHashMap<String, Long>();
        FacetPage<?> fps = this.repository.facetByManyFields(query, new PageRequest(0, 1));
        long total = fps.getTotalElements();
        countMap.put("total", Long.valueOf(total));
        facetsMap.put("all", countMap);
        if (0 == total) {
            return Collections.singletonMap(FACETS, facetsMap);
        }
        for (Page<FacetFieldEntry> page : fps.getFacetResultPages()) {
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
