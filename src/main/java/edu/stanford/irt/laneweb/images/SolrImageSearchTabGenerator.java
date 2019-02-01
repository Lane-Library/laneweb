package edu.stanford.irt.laneweb.images;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchTabGenerator extends AbstractMarshallingGenerator {

    private NumberFormat nf = NumberFormat.getInstance();

    private String query;

    private SolrImageService service;

    public SolrImageSearchTabGenerator(final SolrImageService service, final Marshaller marshaller) {
        super(marshaller);
        this.service = service;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Map<String, String> copyrights = new HashMap<>();
        FacetPage<Image> facetPage = this.service.facetOnCopyright(this.query);
        Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
        List<FacetFieldEntry> facet = page.getContent();
        for (FacetFieldEntry entry : facet) {
            copyrights.put(entry.getValue(), this.nf.format(entry.getValueCount()));
        }
        marshal(copyrights, xmlConsumer);
    }
}
