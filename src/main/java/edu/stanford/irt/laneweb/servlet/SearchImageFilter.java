package edu.stanford.irt.laneweb.servlet;

/**
 * This filter allow to switch the tab in the image search if the result was equals to 0 to go to a tab that have some result
 *  
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SearchImageFilter  extends AbstractLanewebFilter{

	@Autowired
    private SolrImageService service;
	
	private Map<String,String> copyrightMapping;
	
	 protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
		String sourceOri = request.getParameter("source");
		String auto = request.getParameter("auto");
		if (sourceOri != null && sourceOri.indexOf("images-all") > -1 && !"no".equals(auto)){
			String query = request.getParameter("q");
			Map<String, Long> copyrighToValue = getTabValuesFromSolr(query);
			Object[] keys = copyrighToValue.keySet().toArray();
			Arrays.sort(keys);
			if (copyrighToValue.get(copyrightMapping.get(sourceOri)) == null && copyrighToValue.size() > 0) {
				String source = null;
				if ("0".equals(keys[0])) {
					source = "images-all";
				}
				else if("10".equals(keys[0])) {
					source = "cc-images-all";
				}
				else if("15".equals(keys[0])) {
					source = "pmc-images-all";
				}
				else if("20".equals(keys[0])) {
					source = "rl-images-all";
				}
				String url = request.getRequestURL() + "?" + request.getQueryString();
				response.sendRedirect(url.replace(sourceOri, source));
			}
		}
		chain.doFilter(request, response);
	}
	 
	 private Map<String, Long> getTabValuesFromSolr(String queryTerm){
		 HashMap<String, Long> result = new HashMap<String, Long>();
		 FacetPage<Image> facetPage = service.facetOnCopyright(queryTerm);
	     Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
	     List<FacetFieldEntry> list =   page.getContent();
	     for (FacetFieldEntry facet : list) {
	    	 if(facet.getValueCount() != 0){
	    		 result.put(facet.getValue(), facet.getValueCount());
	    	 }
		}
	    return result;
	 }
	 
	 public void init(FilterConfig filterConfig){
			ServletContext servletContext = filterConfig.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
			autowireCapableBeanFactory.configureBean(this, "edu.stanford.irt.solr.service");
			this.copyrightMapping = new HashMap<String , String>();
			this.copyrightMapping.put("images-all","0");
			this.copyrightMapping.put("cc-images-all","10");
			this.copyrightMapping.put("pmc-images-all", "15");
			this.copyrightMapping.put("rl-images-all", "20");
		}
}
