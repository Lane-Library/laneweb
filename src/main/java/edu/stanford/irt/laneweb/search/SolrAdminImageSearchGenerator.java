package edu.stanford.irt.laneweb.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrAdminImageSearchGenerator extends SolrImageSearchGenerator {

    private static final int TOTAL_ELEMENT_BY_PAGE = 1000;

    private String limit = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public SolrAdminImageSearchGenerator(final SolrImageService service,
            final SAXStrategy<SolrImageSearchResult> saxStrategy) {
        super(service, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.limit = (String) model.get(Model.LIMIT);
    }

    @Override
    protected SolrImageSearchResult doSearch(final String query) {
        return doSearch(query, this.basePath.concat("/secure/admin").concat(this.url.toString()));
    }

    @Override
    protected Page<Image> getPage(final String query) {
        PageRequest page = new PageRequest(this.pageNumber, TOTAL_ELEMENT_BY_PAGE);
        Page<Image> result = null;
        if (this.limit == null || "".equals(this.limit)) {
            result = this.service.adminFindByTitleAndDescription(query, this.copyright, this.resourceId, page);
        } else {
            Date date = null;
            try {
                date = this.sdf.parse(this.limit);
            } catch (ParseException e) {
                throw new LanewebException(e);
            }
            result = this.service.adminFindAllFilterOnCopyrightAndWebsiteIdAndDate(query, this.copyright,
                    this.resourceId, date, page);
        }
        return result;
    }
}
