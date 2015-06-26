package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
        result.put("path", this.basePath.concat("/admin").concat(this.url.toString()));
        return result;
    }
        
   
   @Override
    protected Page<Image> getPage(final String query){
       PageRequest page = new PageRequest(this.pageNumber, TOTAL_ELEMENT_BY_PAGE);
       return service.adminFindByTitleAndDescription(query, this.copyright, this.resourceId, page);
    }
}
