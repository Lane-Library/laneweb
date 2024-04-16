package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;

@Controller
public class FacetSuggestionController {

    private static final String RESULT_NOT_FOUND_MESSAGE = "No match found";

    private EresourceFacetService restFacetService;

    private static final int FACET_MIN_COUNT = 1;

    public FacetSuggestionController(final EresourceFacetService solrFacetService) {
        this.restFacetService = solrFacetService;
    }

    @GetMapping(value = "/apps/solr/facet/suggest")
    @ResponseBody
    public Collection<String> getSuggestions(@RequestParam(name = "contains") final String containsTerm,
            @RequestParam(name = "q") final String searchQuery, final String facet, final String facets) {
        Map<String, List<FacetFieldEntry>> facetPage = this.restFacetService.facetByFieldContains(containsTerm,
                searchQuery, facet, facets, FACET_MIN_COUNT);
        List<FacetFieldEntry> facetFieldEntry = facetPage.get(facet);
        List<String> facetsResult = facetFieldEntry.stream().map(FacetFieldEntry::getValue).toList();
        if (facetsResult.isEmpty()) {
            return Collections.singleton(RESULT_NOT_FOUND_MESSAGE);
        }
        return facetsResult;
    }
}
