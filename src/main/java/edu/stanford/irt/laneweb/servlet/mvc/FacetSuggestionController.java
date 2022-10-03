package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.search.FacetService;

@Controller
public class FacetSuggestionController {

    private static final String RESULT_NOT_FOUND_MESSAGE="No match found";
    
    private FacetService solrFacetService;

    private static final int FACET_MIN_COUNT = 1;
    
    
    public FacetSuggestionController(final FacetService solrFacetService) {
        this.solrFacetService = solrFacetService;
    }

    @GetMapping(value = "/apps/solr/facet/suggest")
    @ResponseBody
    public Collection<String> getSuggestions(@RequestParam(name="contains") final String containsTerm, 
            @RequestParam(name = "q") final String searchQuery, final String facet, final String facets) {
        FacetPage<Eresource> facetPage = this.solrFacetService.facetByFieldContains( containsTerm,  searchQuery, facet, facets, FACET_MIN_COUNT);
        List<FacetFieldEntry> facetFieldEntry = facetPage.getFacetResultPage(facet).getContent();
        List<String> facetsResult = facetFieldEntry.stream().map(FacetFieldEntry::getValue).collect(Collectors.toList());
        if(facetsResult.isEmpty()) {
            return Collections.singleton( RESULT_NOT_FOUND_MESSAGE);
        }
        return  facetsResult;
    }
}