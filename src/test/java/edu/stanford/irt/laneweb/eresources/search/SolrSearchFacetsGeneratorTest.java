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

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetService;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetsGenerator;
import edu.stanford.irt.laneweb.model.Model;

public class SolrSearchFacetsGeneratorTest {

    Collection<Page<FacetFieldEntry>> facetPages;

    private FacetPage<Eresource> eresourcesPage;

    private FacetsGenerator generator;

    private Map<String, Object> model;

    private Page<FacetFieldEntry> pageFieldFacet;

    private FacetService service;
    
   
    
    SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy;

    @Before
    public void setUp() {
        this.service = mock(FacetService.class);
        this.facetSAXStrategy = mock(SAXStrategy.class);
        this.pageFieldFacet = mock(Page.class);
        this.eresourcesPage = mock(FacetPage.class);
        this.facetPages = mock(Collection.class);
        this.model = new HashMap<String, Object>();
        this.model.put(Model.QUERY, "skin");
    }

    @Test
    public final void testSimpleDoGenerate() throws Exception {
        Collection<String> publicationTypes = Arrays.asList("Required1");
        Collection<String> facets = Arrays.asList("publicationType");
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 4, publicationTypes);
        this.generator.setFacet(facets);
        this.generator.setModel(model);
        SimpleField field = new SimpleField("publicationType");
        expect(this.service.facetByManyFields("skin", "", 5)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.getFacetResultPage("publicationType")).andReturn(this.pageFieldFacet);
        expect(this.pageFieldFacet.getContent())
                .andReturn(Arrays.asList(new SimpleFacetFieldEntry(field, "Required1", 100)));
        replay(this.service, this.eresourcesPage, this.pageFieldFacet);
        this.generator.generate();
        verify(this.service, this.eresourcesPage, this.pageFieldFacet);
    }
    
    @Test
    public final void testNotEnoughtPublicationResult() throws Exception {
        Collection<String> facets = Arrays.asList("type", "publicationType");
        Collection<String> publicationTypes = Arrays.asList("Required1", "Required2");
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 4, publicationTypes);
        this.generator.setFacet(facets);
        this.generator.setModel(model);
        SimpleField fieldType = new SimpleField("type");
        SimpleField fieldPublicationType = new SimpleField("publicationType");
        List<FacetFieldEntry> resultFromSolr = new ArrayList<FacetFieldEntry>();
        resultFromSolr.add(new SimpleFacetFieldEntry(fieldType, "index", 10));
        resultFromSolr.add(new SimpleFacetFieldEntry(fieldPublicationType, "requiredIndex", 100));
        
        expect(this.service.facetByManyFields("skin", "", 5)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.getFacetResultPage("type")).andReturn(this.pageFieldFacet);
        expect(this.eresourcesPage.getFacetResultPage("publicationType")).andReturn(this.pageFieldFacet);
        expect(this.pageFieldFacet.getContent()).andReturn(resultFromSolr).times(3);
        
        //Second call to the facetService
        expect(this.service.facetByField("skin", "","publicationType", 1000, 1, FacetSort.COUNT)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.getFacetResultPage("publicationType")).andReturn(this.pageFieldFacet);
        
        replay(this.service, this.eresourcesPage, this.pageFieldFacet);
        this.generator.generate();
        verify(this.service, this.eresourcesPage, this.pageFieldFacet);
    }
    
    
}
