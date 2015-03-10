package edu.stanford.irt.laneweb.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchTabGenerator  extends AbstractMarshallingGenerator implements ModelAware {

    private Map<String, Long> copyrights = null;

    private SolrImageService service;
    
    public SolrImageSearchTabGenerator(final Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    public void doGenerate(final XMLConsumer xmlConsumer) {
        marshall(this.copyrights, xmlConsumer);
    }

    public void setModel(final Map<String, Object> model) {
		this.copyrights = new HashMap<String, Long>();
		String searchTerm = ModelUtil.getString(model, Model.QUERY);
		FacetPage<Image> facetPage = service.facetOnCopyright(searchTerm);
		Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
		List<FacetFieldEntry> facet = page.getContent();
		for (FacetFieldEntry entry : facet) {
			this.copyrights.put(entry.getValue(), entry.getValueCount());
		}
	}
    
    
    public void setService(SolrImageService service){
    	this.service =  service;
    }
	
}
