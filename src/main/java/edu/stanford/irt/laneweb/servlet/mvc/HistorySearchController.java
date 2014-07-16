package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * A Controller that provides history metasearch results
 */
@Controller
public class HistorySearchController {

    private static final String ENGINES = "wilson_somhistory,openlibrary-LegacyofMedical002,openlibrary-FirstHundredCombined,openlibrary-25years002,openlibrary-CooperBarkan1954001,openlibrary-Recollections005,openlibrary-Rytand,openlibrary-Pizzo,openlibrary-Versailles,openlibrary-Reinventing,openlibrary-CulturalResources,openlibrary-HistoryOfLane19121967,openlibrary-Whitfield,openlibrary-Challenge,openlibrary-Alway,openlibrary-Dedication006";

    private CompositeDataBinder dataBinder;

    private Collection<String> engines;

    private MetaSearchManager manager;

    @Autowired
    public HistorySearchController(final MetaSearchManager manager, final CompositeDataBinder dataBinder) {
        this.manager = manager;
        this.dataBinder = dataBinder;
        this.engines = new LinkedList<String>(Arrays.asList(ENGINES.split(",")));
    }

    /**
     * Do a metasearch for a query and return a map of results for the history engines.
     *
     * @param query
     *            the query
     * @return a Map representation of the Result for jsonification
     */
    @RequestMapping(value = "/apps/historySearch")
    @ResponseBody
    public Map<String, Object> search(@ModelAttribute(Model.QUERY) final String query) {
        Query simpleQuery = new SimpleQuery(query, this.engines);
        Result result = this.manager.search(simpleQuery, 60000, true);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", result.getStatus());
        resultMap.put("resources", getResourceResultMap(result));
        return resultMap;
    }

    /**
     * puts the query into the model.
     *
     * @param request
     *            the request
     * @param model
     *            the model
     */
    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.dataBinder.bind(model.asMap(), request);
    }

    /**
     * Converts each resource result into a map entry, the key is the id and the value is another map with status, url
     * and hits if avaliable
     *
     * @param result
     *            the parent Result
     * @return a map of resource results
     */
    private Map<String, Map<String, Object>> getResourceResultMap(final Result result) {
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (Result engine : result.getChildren()) {
            for (Result resource : engine.getChildren()) {
                String id = resource.getId();
                Map<String, Object> resourceMap = new HashMap<String, Object>();
                map.put(id, resourceMap);
                resourceMap.put("status", resource.getStatus());
                resourceMap.put("url", resource.getURL());
                resourceMap.put("description", resource.getDescription());
                String hitsString = resource.getHits();
                if (hitsString != null) {
                    resourceMap.put("hits", Integer.parseInt(hitsString));
                }
            }
        }
        return map;
    }
}
