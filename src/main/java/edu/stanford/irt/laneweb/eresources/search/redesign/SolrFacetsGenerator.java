package edu.stanford.irt.laneweb.eresources.search.redesign;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrFacetService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class SolrFacetsGenerator extends AbstractGenerator {

    private static final Logger log = LoggerFactory.getLogger(SolrFacetsGenerator.class);

    private static final String EMPTY = "";

    private static final String PUBLICATION_TYPE = "publicationType";

    private String query;

    private String facets;

    private int facetsToShowSearch;

    private SolrFacetService service;

    private Collection<String> prioritizedPublicationTypes;

    private SAXStrategy<Map<String, Collection<FacetFieldEntry>>> saxStrategy;

    private FacetComparator facetComparator;

    private Map<String, Collection<FacetFieldEntry>> facetFieldEntries;

    public SolrFacetsGenerator(final SolrFacetService service,
            final SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSolrSAXStrategy,
            final int facetsToShowSearch, final Collection<String> publicationTypes) {
        this.service = service;
        this.saxStrategy = facetSolrSAXStrategy;
        this.facetsToShowSearch = facetsToShowSearch + 1;
        this.prioritizedPublicationTypes = publicationTypes;
        this.facetComparator = new FacetComparator(publicationTypes);
        this.facetFieldEntries = new HashMap<>();
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.facets = ModelUtil.getString(model, Model.FACETS, EMPTY);
        this.facetComparator.addPrioritiesFromFacets(facets);
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try {
            // addYearToFacets();
            FacetPage<Eresource> fps = this.service.facetByManyFields(this.query, this.facets, this.facetsToShowSearch);
            orderFacets(fps);
            maybeRequestMorePublicationTypes();
            this.saxStrategy.toSAX(this.facetFieldEntries, xmlConsumer);
        } catch (UncategorizedSolrException e) {
            log.error(e.toString());
            // marshal("searching for facets failed", xmlConsumer);
        }
    }

    private void orderFacets(FacetPage<Eresource> fps) {
        for (String facetName : SolrFacetService.FACET_FIELDS) {
            Page<FacetFieldEntry> facetPage = fps.getFacetResultPage(facetName);
            orderFacet(facetName, facetPage);
        }
    }

    private void orderFacet(String facetName, Page<FacetFieldEntry> facetPage) {
        List<FacetFieldEntry> entryList = facetPage.getContent();
        Collection<FacetFieldEntry> orderedFacets = new TreeSet<>(this.facetComparator);
        orderedFacets.addAll(entryList);
        this.facetFieldEntries.put(facetName, orderedFacets);
    }

    private void maybeRequestMorePublicationTypes() {
        Collection<FacetFieldEntry> facetList = this.facetFieldEntries.get(PUBLICATION_TYPE);
        long count = facetList.stream()
                .filter((final FacetFieldEntry s) -> this.prioritizedPublicationTypes.contains(s.getValue())).count();
        if (count < this.prioritizedPublicationTypes.size()) {
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, PUBLICATION_TYPE, 0, 1000, 1,
                    FacetSort.COUNT);
            Page<FacetFieldEntry> facetPage = fps.getFacetResultPage(PUBLICATION_TYPE);
            orderFacet(PUBLICATION_TYPE, facetPage);
        }
    }
}
