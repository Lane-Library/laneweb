package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGenerator extends AbstractSearchGenerator<Page<Image>>{

    private static final int TOTAL_ELEMENTS_BY_PAGE = 52;

    
    private SolrImageService service;

    private String copyright = "0";
    
    private int pageNumber = 0;
    
    public SolrImageSearchGenerator(final SolrImageService service, final SAXStrategy<Page<Image>> saxStrategy) {
        super( saxStrategy);
        this.service = service;
    }

    @Override
    protected Page<Image> doSearch(String query) {
        Pageable page = new PageRequest(pageNumber, TOTAL_ELEMENTS_BY_PAGE); 
        return  service.findByTitleOrDescriptionFilterOnCopyright(query, this.copyright, page);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE);
        if(page != null){
            this.pageNumber = Integer.valueOf(page);
        }
        String category = ModelUtil.getString(model, Model.CATEGORY);
        if(category != null){
            this.copyright = category;
        }
    }

    

    

}
