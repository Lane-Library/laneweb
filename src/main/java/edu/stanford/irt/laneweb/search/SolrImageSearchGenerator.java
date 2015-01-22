package edu.stanford.irt.laneweb.search;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGenerator extends AbstractSearchGenerator <Map<String,Object>>{

    private SolrImageService service;
    
    private static final String[] TAB_CONTENT = {"Broad Reuse Rights",
                                                 "Defined Reuse Rights",
                                                 "Limited Reuse Rights",
                                                 "Possibly CC Rights"};
    
    private static final int TOTAL_ELEMENT_BY_PAGE = 52;
    
    private String copyright = "0";
    
    private int pageNumber = 0;

    private String queryString;
    
    private String tab = TAB_CONTENT[0];
    
    private String searchTerm;
    
    private String source; 
    
    public SolrImageSearchGenerator(final SolrImageService service, final SAXStrategy<Map<String,Object>> saxStrategy) {
        super( saxStrategy);
        this.service = service;
    }

    @Override
    protected Map<String,Object> doSearch(String query) {
        Map<String, Object> result = new HashMap<String, Object>();
        Pageable page = new PageRequest(pageNumber, TOTAL_ELEMENT_BY_PAGE); 
        Page<Image> pageResult = service.findByTitleOrDescriptionFilterOnCopyright(query, this.copyright, page);
        result.put("page", pageResult);
        result.put("path", this.queryString);
        result.put( Model.QUERY, this.searchTerm);
        result.put("tab", this.tab);
        result.put(Model.SOURCE, this.source);
        return result;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE);
        if(page != null){
            this.pageNumber = Integer.valueOf( page) -1;
        } 
        searchTerm = ModelUtil.getString(model, Model.QUERY);
        source = ModelUtil.getString(model, Model.SOURCE);
        this.queryString = "/search.html?q="+searchTerm+"&source="+source+"&page=";
        if(source != null){
            if(source.startsWith("cc-")){
                this.copyright = "10";
                this.tab = TAB_CONTENT[1];
            }else if(source.startsWith("pmc-")){
                this.copyright = "15";
                this.tab = TAB_CONTENT[2];
            }else if(source.startsWith("rl-")){
                this.copyright = "20";
                this.tab = TAB_CONTENT[3];
            }
        }
    }

}
