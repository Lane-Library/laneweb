package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.search.MetaSearchService;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * A Controller that provides metasearch results
 */
@Controller
public class MetaSearchController {

    private static final int ONE_MINUTE = 60000;

    private DataBinder dataBinder;

    private MetaSearchService metaSearchService;

    @Autowired
    public MetaSearchController(final MetaSearchService metaSearchService,
            @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder") final DataBinder dataBinder) {
        this.metaSearchService = metaSearchService;
        this.dataBinder = dataBinder;
    }

    /**
     * Do a metasearch for a query and return only the requested resources.
     *
     * @param query
     *            the query
     * @param resources
     *            the desired resources to search
     * @param proxyLinks
     *            whether to proxy the url
     * @param baseProxyURL
     *            the text to preprend for proxied links
     * @return a Map representation of the Result for jsonification
     */
    @RequestMapping(value = "/apps/resourceSearch")
    @ResponseBody
    public Map<String, Object> search(@ModelAttribute(Model.QUERY) final String query,
            @ModelAttribute(Model.RESOURCES) final List<String> resources,
            @ModelAttribute(Model.PROXY_LINKS) final boolean proxyLinks,
            @ModelAttribute(Model.BASE_PROXY_URL) final String baseProxyURL) {
        Collection<String> engines = getEnginesForResources(resources);
        Query simpleQuery = new SimpleQuery(query);
        Result result = this.metaSearchService.search(simpleQuery, engines, ONE_MINUTE);
        Map<String, Object> resultMap = getMapForResult(result, resources);
        if (proxyLinks) {
            createProxyLinks(resultMap, baseProxyURL);
        }
        return resultMap;
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

    /**
     * Converts the urls to proxy links
     *
     * @param resultMap
     *            the result map
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
        Collection<String> engines = new ArrayList<>();
        Result describeResult = this.metaSearchService.describe(new SimpleQuery(""), null);
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
        Map<String, Object> map = new HashMap<>();
        SearchStatus status;
        Collection<Result> children;
        synchronized (result) {
            status = result.getStatus();
            children = result.getChildren();
        }
        map.put("status", status);
        map.put("resources", getResourceResultMap(children, resources));
        return map;
    }

    /**
     * Creates a map from a Result with status, url and hits if available.
     *
     * @param resource
     *            the Result
     * @return a Map
     */
    private Map<String, Object> getResourceMap(final Result resource) {
        Map<String, Object> resourceMap = new HashMap<>();
        resourceMap.put("status", resource.getStatus());
        resourceMap.put("url", resource.getURL());
        String hitsString = resource.getHits();
        if (hitsString != null) {
            resourceMap.put("hits", Integer.parseInt(hitsString));
        }
        return resourceMap;
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
        Map<String, Map<String, Object>> map = new HashMap<>();
        Collection<Result> children;
        for (Result engine : engines) {
            synchronized (engine) {
                children = engine.getChildren();
            }
            for (Result resource : children) {
                String id = resource.getId();
                if (resources.contains(id)) {
                    map.put(id, getResourceMap(resource));
                }
            }
        }
        return map;
    }
}
