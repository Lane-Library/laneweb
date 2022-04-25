package edu.stanford.irt.laneweb.images;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.search.AbstractSearchGenerator;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGenerator extends AbstractSearchGenerator<SolrImageSearchResult> {

    private static final int BROAD_REUSE_RIGHTS = 1;

    private static final int MAX_REUSE_RIGHTS = 0;

    private static final int POSSIBLE_REUSE_RIGHTS = 2;

    private static final int RESTRICTIVE_REUSE_RIGHTS = 3;

    private static final String[] TAB_CONTENT = { "Maximum Reuse Rights", "Broad Reuse Rights", "Possible Reuse Rights",
            "Restrictive Reuse Rights" };

    private static final int TOTAL_ELEMENT_BY_PAGE = 50;

    protected String basePath;

    protected String copyright = "0";

    protected int pageNumber;

    protected String resourceId;

    protected SolrImageService service;

    protected String url;

    private String source;

    private String tab = TAB_CONTENT[MAX_REUSE_RIGHTS];

    public SolrImageSearchGenerator(final SolrImageService service,
            final SAXStrategy<SolrImageSearchResult> saxStrategy) {
        super(saxStrategy);
        this.service = service;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.parseInt(page) - 1;
        }
        this.resourceId = ModelUtil.getString(model, Model.RESOURCE_ID);
        this.source = ModelUtil.getString(model, Model.SOURCE, "images-");
        this.basePath = ModelUtil.getString(model, Model.BASE_PATH, "");
        String query = ModelUtil.getString(model, Model.QUERY);
        this.url = "/search.html?q=" + query + "&source=" + this.source;
        if (this.source != null) {
            if (this.source.startsWith("cc-")) {
                this.copyright = "10";
                this.tab = TAB_CONTENT[BROAD_REUSE_RIGHTS];
            } else if (this.source.startsWith("pmc-")) {
                this.copyright = "15";
                this.tab = TAB_CONTENT[POSSIBLE_REUSE_RIGHTS];
            } else if (this.source.startsWith("rl-")) {
                this.copyright = "20";
                this.tab = TAB_CONTENT[RESTRICTIVE_REUSE_RIGHTS];
            }
        }
    }

    @Override
    protected SolrImageSearchResult doSearch(final String query) {
        return doSearch(query, this.basePath + this.url);
    }

    protected SolrImageSearchResult doSearch(final String query, final String path) {
        Page<Image> pageResult = getPage(query);
        FacetPage<Image> facetPage = this.service.facetOnWebsiteId(query, this.copyright);
        Page<FacetFieldEntry> facet = facetPage.getFacetResultPage("websiteId");
        return new SolrImageSearchResult(query, pageResult, this.resourceId, facet, this.basePath + this.url, this.tab,
                this.source);
    }

    @Override
    protected SolrImageSearchResult getEmptyResult() {
        return SolrImageSearchResult.EMPTY_RESULT;
    }

    protected Page<Image> getPage(final String query) {
        Page<Image> pageResult;
        Pageable page = PageRequest.of(this.pageNumber, TOTAL_ELEMENT_BY_PAGE);
        if (this.resourceId == null) {
            pageResult = this.service.findByTitleAndDescriptionFilterOnCopyright(query, this.copyright, page);
        } else {
            pageResult = this.service.findByTitleAndDescriptionFilterOnCopyrightAndWebsiteId(query, this.copyright,
                    this.resourceId, page);
        }
        return pageResult;
    }
}
