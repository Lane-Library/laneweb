package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This filter allow to switch the tab in the image search if the result was equals to 0 to go to a tab that have some
 * result
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.images.FacetFieldComparator;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SearchImageInterceptor extends HandlerInterceptorAdapter {

	private static final String ALL_IMAGES = "images-all";

	private static final String CC_IMAGES = "cc-images-all";

	private static final String PMC_IMAGES = "pmc-images-all";

	private static final String RL_IMAGES = "rl-images-all";

	private Map<String, String> copyrightMapping;

	private SolrImageService service;

	public SearchImageInterceptor(final SolrImageService service) {
		this.service = service;
		this.copyrightMapping = new HashMap<>();
		this.copyrightMapping.put("0", ALL_IMAGES);
		this.copyrightMapping.put("10", CC_IMAGES);
		this.copyrightMapping.put("15", PMC_IMAGES);
		this.copyrightMapping.put("20", RL_IMAGES);
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws IOException {
		String sourceOri = request.getParameter("source");
		String auto = request.getParameter("auto");
		if (sourceOri != null && sourceOri.indexOf(ALL_IMAGES) > -1 && !"no".equals(auto)) {
			String query = request.getParameter("q");
			String copyrightName = this.getTabValuesFromSolr(query, sourceOri);
			String source = copyrightMapping.get(copyrightName);
			if (null != copyrightName && null != source && !source.equals(sourceOri)) {
				String url = request.getRequestURL() + "?" + request.getQueryString();
				response.sendRedirect(url.replace(sourceOri, source).concat("&auto=no"));
				return false;
			}
		}
		return true;
	}

	private String getTabValuesFromSolr(final String queryTerm, String sourceOri) {
		FacetPage<Image> facetPage = this.service.facetOnCopyright(queryTerm);
		Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
		List<FacetFieldEntry> facetList = new ArrayList<>( page.getContent());
		Collections.sort(facetList, new FacetFieldComparator() );
		for (FacetFieldEntry facet : facetList) {
			long copyrightValue = facet.getValueCount();
			if (copyrightValue > 0) {
				return facet.getValue();
			}
		}
		return null;
	}


}
