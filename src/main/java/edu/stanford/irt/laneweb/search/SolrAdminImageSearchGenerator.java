package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;


public class SolrAdminImageSearchGenerator extends SolrImageSearchGenerator{

    private static final int TOTAL_ELEMENT_BY_PAGE = 1000;

    
    public SolrAdminImageSearchGenerator(final SolrImageService service, final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(service, saxStrategy);        
    }
    
    @Override
    protected Map<String, Object> doSearch(final String query) {
        Map<String, Object> result = super.doSearch(query);
        String pa result.get("path");
        
        return result;
    }
    
   
   @Override
    protected Page<Image> getPage(final String query){
        Page<Image> pageResult = null;
        Pageable page = new PageRequest(this.pageNumber, TOTAL_ELEMENT_BY_PAGE);
        if (this.resourceId == null) {
            pageResult = this.service.findByTitleAndDescriptionFilterOnCopyright(query, this.copyright, page);
        } else {
            pageResult = this.service.findByTitleAndDescriptionFilterOnCopyrightAndWebsiteId(query, this.copyright, this.resourceId, page);
        }
        return pageResult;
    }
}
