package edu.stanford.irt.laneweb.images;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrAdminImageSearchGenerator extends SolrImageSearchGenerator {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final int TOTAL_ELEMENT_BY_PAGE = 1000;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private String limit;

    public SolrAdminImageSearchGenerator(final SolrImageService service,
            final SAXStrategy<SolrImageSearchResult> saxStrategy) {
        super(service, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.limit = ModelUtil.getString(model, Model.LIMIT);
    }

    @Override
    protected SolrImageSearchResult doSearch(final String query) {
        return doSearch(query, this.basePath.concat("/secure/admin").concat(this.url));
    }

    @Override
    protected Page<Image> getPage(final String query) {
        PageRequest page =  PageRequest.of(this.pageNumber, TOTAL_ELEMENT_BY_PAGE);
        Page<Image> result;
        if (this.limit == null || "".equals(this.limit)) {
            result = this.service.adminFindByTitleAndDescription(query, this.copyright, this.resourceId, page);
        } else {
            java.util.Date date = java.util.Date
                    .from(LocalDate.parse(this.limit, this.formatter).atStartOfDay(AMERICA_LA).toInstant());
            result = this.service.adminFindAllFilterOnCopyrightAndWebsiteIdAndDate(query, this.copyright,
                    this.resourceId, date, page);
        }
        return result;
    }
}
