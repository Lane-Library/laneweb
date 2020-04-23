package edu.stanford.irt.laneweb.eresources.browse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXResult;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;

/**
 * A generator that returns from Solr a faceted list of MeSH with hit counts, limited by resource type
 */
public class SubjectBrowseGenerator extends AbstractBrowseGenerator {

    // number of facet values to return from Solr: must be large enough to fetch all mesh (high cardinality)
    private static final int MAX_FACETS = 20_000;

    private Marshaller marshaller;

    public SubjectBrowseGenerator(final String componentType, final SolrService service, final Marshaller marshaller) {
        super(componentType, service);
        this.marshaller = marshaller;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (null != this.browseQuery) {
            FacetPage<Eresource> fps = this.solrService.facetByField(BASE_BROWSE_QUERY, this.browseQuery, "mesh", 0,
                    MAX_FACETS, 1, FacetSort.INDEX);
            Map<String, Long> map = new HashMap<>();
            for (Page<FacetFieldEntry> page : fps.getFacetResultPages()) {
                for (FacetFieldEntry entry : page) {
                    map.put(entry.getValue(), entry.getValueCount());
                }
            }
            marshal(map, xmlConsumer);
        }
    }

    private void marshal(final Object object, final XMLConsumer xmlConsumer) {
        try {
            this.marshaller.marshal(object, new SAXResult(xmlConsumer));
        } catch (IOException | XmlMappingException e) {
            throw new LanewebException(e);
        }
    }
}
