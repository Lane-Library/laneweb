package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * A Controller that provides metasearch results
 */
@Controller
public class MetaSearchController {

    @Autowired
    private MetaSearchManager<Result> manager;

    @Autowired
    private CompositeDataBinder dataBinder;
    
    public MetaSearchController() {}

    public MetaSearchController(final MetaSearchManager<Result> manager, final CompositeDataBinder dataBinder) {
        this.manager = manager;
        this.dataBinder = dataBinder;
    }

    /**
     * Do a metasearch for a query and return only the requested resources.
     * 
     * @param query
     *            the query
     * @param resources
     *            the desired resources to search
     * @return a Map representation of the Result for jsonification
     */
    @RequestMapping(value = "/apps/resourceSearch")
    @ResponseBody
    public Map<String, Object> search(@ModelAttribute(Model.QUERY) final String query,
            @ModelAttribute(Model.RESOURCES) final List<String> resources,
            @ModelAttribute(Model.PROXY_LINKS) final boolean proxyLinks,
            @ModelAttribute(Model.BASE_PROXY_URL) final String baseProxyURL) {
        Query simpleQuery = new SimpleQuery(query);
        Collection<String> engines = getEnginesForResources(resources);
        Result result = this.manager.search(simpleQuery, 60000, engines, false);
        Map<String, Object> resultMap = getMapForResult(result, resources);
        if (proxyLinks) {
            createProxyLinks(resultMap, baseProxyURL);
        }
        return resultMap;
    }

    /**
     * Converts the urls to proxy links
     * 
     * @param resultMap
     *            the result map
     * @param ipgroup
     *            the IPGroup
     * @param sunetid
     *            the sunetid
     * @param ticket
     *            the Ticket
     */
    @SuppressWarnings("unchecked")
    private void createProxyLinks(final Map<String, Object> resultMap, final String baseProxyURL) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> resources = (Map<String, Object>) resultMap.get("resources");
        for (Object value : resources.values()) {
            sb.setLength(0);
            Map<String, Object> resource = (Map<String, Object>) value;
            sb.append(baseProxyURL).append(resource.get("url"));
            resource.put("url", sb.toString());
        }
    }

    /**
     * Gets a list of engines that contain all the resources.
     * 
     * @param resources
     * @return a list of engines
     */
    private Collection<String> getEnginesForResources(final List<String> resources) {
        Collection<String> engines = new LinkedList<String>();
        Result describeResult = this.manager.describe(new SimpleQuery(""), resources);
        for (Result engine : describeResult.getChildren()) {
            for (Result resource : engine.getChildren()) {
                if (resources.contains(resource.getId())) {
                    engines.add(engine.getId());
                    break;
                }
            }
        }
        return engines;
    }

    /**
     * Converts the Result into a map for jsonification
     * 
     * @param result
     *            the Result to convert
     * @param resources
     *            the resources requested
     * @return a Map representation of the Result with only the requested resources
     */
    private Map<String, Object> getMapForResult(final Result result, final List<String> resources) {
        Map<String, Object> map = new HashMap<String, Object>();
        // synchronized because otherwise search might update the result while
        // this is happening
        synchronized (result) {
            map.put("status", result.getStatus());
            map.put("resources", getResourceResultMap(result.getChildren(), resources));
        }
        return map;
    }

    /**
     * Converts each resource result into a map entry, the key is the id and the value is another map with status, url
     * and hits if avaliable
     * 
     * @param engines
     *            a list of engine results
     * @param resources
     *            the resource list
     * @return a map of resource results
     */
    private Map<String, Map<String, Object>> getResourceResultMap(final Collection<Result> engines,
            final List<String> resources) {
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (Result engine : engines) {
            for (Result resource : engine.getChildren()) {
                String id = resource.getId();
                if (resources.contains(id)) {
                    Map<String, Object> resourceMap = new HashMap<String, Object>();
                    map.put(id, resourceMap);
                    resourceMap.put("status", resource.getStatus());
                    resourceMap.put("url", resource.getURL());
                    String hitsString = resource.getHits();
                    if (hitsString != null) {
                        resourceMap.put("hits", Integer.parseInt(hitsString));
                    }
                }
            }
        }
        return map;
    }

    /**
     * puts the q and r request parameters into the model.
     * 
     * @param request
     *            the request
     * @param model
     *            the model
     */
    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.dataBinder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.PROXY_LINKS)) {
            model.addAttribute(Model.PROXY_LINKS, Boolean.FALSE);
        }
        if (!model.containsAttribute(Model.BASE_PROXY_URL)) {
            model.addAttribute(Model.BASE_PROXY_URL, null);
        }
    }
}
