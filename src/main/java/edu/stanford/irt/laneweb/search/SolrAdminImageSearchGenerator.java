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


public class SolrAdminImageSearchGenerator extends SolrImageSearchGenerator{

    private static final int TOTAL_ELEMENT_BY_PAGE = 1000;

    private String limit = null;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    
    
    
    public SolrAdminImageSearchGenerator(final SolrImageService service, final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(service, saxStrategy);        
    }
    
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameters =  (Map<String, String[]>) model.get(Model.PARAMETER_MAP);
        String[] limit = parameters.get(Model.LIMIT);  
        if(limit != null && limit.length >0){
            this.limit = limit[0];
        }else{
            this.limit = null;
        }
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
       Page<Image> result = null;
       if(this.limit == null || "".equals(limit)){
           result = service.adminFindByTitleAndDescription(query, this.copyright, this.resourceId, page);
       }else{
           Date date = null;
            try {
                date = sdf.parse(limit);
             } catch (ParseException e) {
                 throw new LanewebException(e);
            }
           result = service.adminFindAllFilterOnCopyrightAndWebsiteIdAndDate(query, this.copyright, this.resourceId, date ,page);
       }
       return result;
    }
}
