package edu.stanford.irt.laneweb.servlet;

/**
 * This filter allow to switch the tab in the image search if the result was equals to 0 to go to a tab that have some
 * result
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SearchImageFilter extends AbstractLanewebFilter {

    private static final String ALL_IMAGES = "images-all";

    private static final String CC_IMAGES = "cc-images-all";

    private static final String PMC_IMAGES = "pmc-images-all";

    private static final String RL_IMAGES = "rl-images-all";

    private Map<String, String> copyrightMapping;

    private SolrImageService service;

    @Override
    public void init(final FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        this.service = webApplicationContext.getBean("edu.stanford.irt.solr.service", SolrImageService.class);
        this.copyrightMapping = new HashMap<>();
        this.copyrightMapping.put(ALL_IMAGES, "0");
        this.copyrightMapping.put(CC_IMAGES, "10");
        this.copyrightMapping.put(PMC_IMAGES, "15");
        this.copyrightMapping.put(RL_IMAGES, "20");
    }

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        String sourceOri = request.getParameter("source");
        String auto = request.getParameter("auto");
        if (sourceOri != null && sourceOri.indexOf(ALL_IMAGES) > -1 && !"no".equals(auto)) {
            String query = request.getParameter("q");
            Map<String, Long> copyrighToValue = getTabValuesFromSolr(query);
            Object[] keys = copyrighToValue.keySet().toArray();
            Arrays.sort(keys);
            if (copyrighToValue.get(this.copyrightMapping.get(sourceOri)) == null && !copyrighToValue.isEmpty()) {
                String source = getNewSource(keys);
                String url = request.getRequestURL() + "?" + request.getQueryString();
                response.sendRedirect(url.replace(sourceOri, source));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private String getNewSource(final Object[] keys) {
        String source = "";
        if ("0".equals(keys[0])) {
            source = ALL_IMAGES;
        } else if ("10".equals(keys[0])) {
            source = CC_IMAGES;
        } else if ("15".equals(keys[0])) {
            source = PMC_IMAGES;
        } else if ("20".equals(keys[0])) {
            source = RL_IMAGES;
        }
        return source;
    }

    private Map<String, Long> getTabValuesFromSolr(final String queryTerm) {
        Map<String, Long> result = new HashMap<>();
        FacetPage<Image> facetPage = this.service.facetOnCopyright(queryTerm);
        Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
        List<FacetFieldEntry> list = page.getContent();
        for (FacetFieldEntry facet : list) {
            if (facet.getValueCount() != 0) {
                result.put(facet.getValue(), facet.getValueCount());
            }
        }
        return result;
    }
}
