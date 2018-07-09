package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

@Controller
public class JsonImageSearchController {

    private SolrImageService service;

    public JsonImageSearchController(final SolrImageService service) {
        this.service = service;
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Image getImage(final String id) {
        return this.service.findById(id);
    }

    @RequestMapping(value = "/facet/images/copyright", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<FacetFieldEntry> getImageFacet(final String query) {
        FacetPage<Image> facetPage = this.service.facetOnCopyright(query);
        Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
        return page.getContent();
    }

    @RequestMapping(value = "/secure/image/update", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Image updateImage(final String id) {
        Image image = this.service.adminFindById(id);
        String websiteId = id.substring(0, id.indexOf('/'));
        image.setWebsiteId(websiteId);
        boolean isEnable = image.isEnable();
        if (isEnable) {
            image.setEnable(false);
        } else {
            image.setEnable(true);
        }
        this.service.saveImage(image);
        return image;
    }
}
