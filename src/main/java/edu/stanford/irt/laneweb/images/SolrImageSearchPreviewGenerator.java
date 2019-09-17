package edu.stanford.irt.laneweb.images;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchPreviewGenerator extends AbstractMarshallingGenerator {

    private static final int IMAGES_TO_RETURN = 10;

    private String query;

    private SolrImageService service;

    public SolrImageSearchPreviewGenerator(final Marshaller marshaller, final SolrImageService service) {
        super(marshaller);
        this.service = service;
    }

    @Override
    public void doGenerate(final XMLConsumer xmlConsumer) {
        List<String> result = new ArrayList<>();
        FacetPage<Image> facetPage = this.service.facetOnCopyright(this.query);
        Page<FacetFieldEntry> page = facetPage.getFacetResultPage("copyright");
        List<FacetFieldEntry> facetList = new ArrayList<>(page.getContent());
        Collections.sort(facetList, new FacetFieldComparator());
        for (FacetFieldEntry facet : facetList) {
            if (facet.getValueCount() > 0) {
                Pageable pageRequest = PageRequest.of(0, IMAGES_TO_RETURN);
                Page<Image> pageResult = this.service.findByTitleAndDescriptionFilterOnCopyright(this.query,
                        facet.getValue(), pageRequest);
                result = getImagesSource(pageResult.getContent());
                break;
            }
        }
        marshal(result, xmlConsumer);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getObject(model, Model.QUERY, String.class);
    }

    private List<String> getImagesSource(final List<Image> images) {
        List<String> imageSources = new ArrayList<>();
        for (Image image : images) {
            imageSources.add(image.getThumbnailSrc());
        }
        return imageSources;
    }
}
