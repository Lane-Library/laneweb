package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.BufferedReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

@Controller
public class JsonImageSearchController {

    BufferedReader in = null;

    @Autowired
    @Qualifier("edu.stanford.irt.solr.service")
    private SolrImageService service;

    @RequestMapping(value = "/image", produces = "application/json")
    @ResponseBody
    public Image getImage(final String id) {
        return this.service.findById(id);
    }

    @RequestMapping(value = "/facet/images/copyright", produces = "application/json")
    @ResponseBody
    public List<FacetFieldEntry> getImageFacet(final String query) {
        FacetPage<Image> facetPage = this.service.facetOnCopyright(query);
        Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
        return page.getContent();
    }
}
