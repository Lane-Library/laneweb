package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.irt.laneweb.model.Model;

@WebFilter({ "/search.html" })
public class LaneCatalogSearchRedirectFilter extends AbstractLanewebFilter {

    private static final String CATALOG = "catalog-";

    private static final String ENCODED_LANE_CATALOG_FACET = "recordType%3A%22bib%22";

    private static final String ENCODED_SEPARATOR_PLUS_LANE_CATALOG_FACET = "%3A%3A" + ENCODED_LANE_CATALOG_FACET;

    private static final Pattern FACETS_PATTERN = Pattern.compile("facets=(.+)(&|$)");

    private static final String LANE_CATALOG_FACET = "recordType:\"bib\"";

    private String appendLaneCatalogFacet(final String queryString) {
        StringBuilder sb = new StringBuilder();
        if (queryString.contains("facets=")) {
            sb.append(FACETS_PATTERN.matcher(queryString)
                    .replaceFirst("facets=$1" + ENCODED_SEPARATOR_PLUS_LANE_CATALOG_FACET));
        } else {
            sb.append(queryString);
            sb.append("&facets=");
            sb.append(ENCODED_LANE_CATALOG_FACET);
        }
        return sb.toString();
    }

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        boolean isLaneCatalogSearch = false;
        boolean needsLaneCatalogFacet = true;
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String paraName = names.nextElement();
            if (Model.SOURCE.equals(paraName) && request.getParameter(paraName).startsWith(CATALOG)) {
                isLaneCatalogSearch = true;
            }
            if (Model.FACETS.equals(paraName) && request.getParameter(paraName).contains(LANE_CATALOG_FACET)) {
                needsLaneCatalogFacet = false;
            }
        }
        if (isLaneCatalogSearch && needsLaneCatalogFacet) {
            String redirect = request.getRequestURL() + "?" + appendLaneCatalogFacet(request.getQueryString());
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.addHeader("Location", redirect);
        } else {
            chain.doFilter(request, response);
        }
    }
}
