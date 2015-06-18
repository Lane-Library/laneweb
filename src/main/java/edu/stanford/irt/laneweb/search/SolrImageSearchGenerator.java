package edu.stanford.irt.laneweb.search;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGenerator extends AbstractSearchGenerator<Map<String, Object>> {

    private static final String[] TAB_CONTENT = { "Maximum Reuse Rights", "Broad Reuse Rights",
            "Possible Reuse Rights", "Restrictive Reuse Rights" };

    private static final int TOTAL_ELEMENT_BY_PAGE = 52;

    protected String copyright = "0";

    protected int pageNumber = 0;

    protected String resourceId;

    private String searchTerm;

    protected SolrImageService service;

    private String source;

    private String tab = TAB_CONTENT[0];

    private String url;

    private String basePath;

    public SolrImageSearchGenerator(final SolrImageService service, final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(saxStrategy);
        this.service = service;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page) - 1;
        }
        this.resourceId = ModelUtil.getString(model, Model.RESOURCE_ID);
        this.searchTerm = ModelUtil.getString(model, Model.QUERY);
        this.source = ModelUtil.getString(model, Model.SOURCE);
        this.basePath = ModelUtil.getString(model, Model.BASE_PATH);
        this.url = "/search.html?q=" + this.searchTerm + "&source=" + this.source;
        if (this.source != null) {
            if (this.source.startsWith("cc-")) {
                this.copyright = "10";
                this.tab = TAB_CONTENT[1];
            } else if (this.source.startsWith("pmc-")) {
                this.copyright = "15";
                this.tab = TAB_CONTENT[2];
            } else if (this.source.startsWith("rl-")) {
                this.copyright = "20";
                this.tab = TAB_CONTENT[3];
            }
        }
    }

    @Override
    protected Map<String, Object> doSearch(final String query) {
        Map<String, Object> result = new HashMap<String, Object>();
        Page<Image> pageResult = getPage(query);
        FacetPage<Image> facetPage = this.service.facetOnWebsiteId(query, this.copyright);
        Page<FacetFieldEntry> facet = facetPage.getFacetResultPage("websiteId");
        result.put("page", pageResult);
        result.put("selectedResource", this.resourceId);
        result.put("websiteIdFacet", facet);
        result.put("path", this.basePath.concat(this.url.toString()));
        result.put("tab", this.tab);
        result.put(Model.SOURCE, this.source);
        return result;
    }

    protected Page<Image> getPage(final String query) {
        Page<Image> pageResult = null;
        Pageable page = new PageRequest(this.pageNumber, TOTAL_ELEMENT_BY_PAGE);
        if (this.resourceId == null) {
            pageResult = this.service.findByTitleAndDescriptionFilterOnCopyright(query, this.copyright, page);
        } else {
            pageResult = this.service.findByTitleAndDescriptionFilterOnCopyrightAndWebsiteId(query, this.copyright,
                    this.resourceId, page);
        }
        return pageResult;
    }
}
