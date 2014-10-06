package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

@Controller
public class ImageFacetController {

	@Autowired
	@Qualifier("edu.stanford.irt.solr.service")
	private SolrImageService service;

	@RequestMapping(value = "/facet/images/{facetOn}", produces = "application/json")
	@ResponseBody
	public List<FacetFieldEntry> getImageFacet(String query, @PathVariable String facetOn) {
		FacetPage<Image> facetPage = service.facetOnCopyrightAndWebsiteId(query);
		Page<FacetFieldEntry> page = facetPage.getFacetResultPage(facetOn);
		return page.getContent();
	}
	

}
