package edu.stanford.irt.laneweb.eresources.search;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;
import edu.stanford.irt.laneweb.eresources.model.solr.Field;
import edu.stanford.irt.laneweb.model.Model;

public class SolrSearchFacetsGeneratorTest {

    Collection<Page<FacetFieldEntry>> facetPages;

    private Map<String, List<FacetFieldEntry>> eresourcesPage;

    private FacetsGenerator generator;

    private Map<String, Object> model;

    private List<FacetFieldEntry> pageFieldFacet;

    private EresourceFacetService service;

    SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy;

    @BeforeEach
    public void setUp() {
        this.service = mock(EresourceFacetService.class);
        this.facetSAXStrategy = mock(SAXStrategy.class);
        this.eresourcesPage = mock(Map.class);
        this.facetPages = mock(Collection.class);
        this.model = new HashMap<String, Object>();
        this.model.put(Model.QUERY, "skin");
        this.pageFieldFacet = new ArrayList<>();
    }

    @Test
    public final void testSimpleDoGenerate() throws Exception {
        Collection<String> publicationTypes = Arrays.asList("Required1");
        Collection<String> facets = Arrays.asList("publicationType");
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 20, publicationTypes);
        this.generator.setFacet(facets);
        this.generator.setModel(model);
        expect(this.service.facetByManyFields("skin", "", 20)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.get("publicationType")).andReturn(this.pageFieldFacet);
        replay(this.service, this.eresourcesPage);
        this.generator.generate();
        verify(this.service, this.eresourcesPage);
    }

    @Test
    public final void testNotEnoughtPublicationResult() throws Exception {
        Collection<String> facets = Arrays.asList("type", "publicationType");
        Collection<String> publicationTypes = Arrays.asList("Required1", "Required2");
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 20, publicationTypes);
        this.generator.setFacet(facets);
        this.generator.setModel(model);

        List<FacetFieldEntry> typeFacetList = Arrays.asList(new FacetFieldEntry(new Field("type"), "index", 10));
        List<FacetFieldEntry> publicationTypeFacetList = Arrays
                .asList(new FacetFieldEntry(new Field("publicationType"), "Required1", 100));

        expect(this.eresourcesPage.get("type")).andReturn(typeFacetList);
        expect(this.eresourcesPage.get("publicationType")).andReturn(publicationTypeFacetList);

        expect(this.service.facetByManyFields("skin", "", 20)).andReturn(this.eresourcesPage);

        // Second call to the facetService
        expect(this.service.facetByField("skin", "", "publicationType", 1000, 1, FacetSort.COUNT))
                .andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.get("publicationType")).andReturn(this.pageFieldFacet);

        replay(this.service, this.eresourcesPage);
        this.generator.generate();
        verify(this.service, this.eresourcesPage);
    }

}
