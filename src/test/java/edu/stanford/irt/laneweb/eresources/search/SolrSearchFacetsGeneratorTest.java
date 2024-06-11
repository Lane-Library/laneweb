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

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;
import edu.stanford.irt.laneweb.eresources.model.solr.Field;
import edu.stanford.irt.laneweb.model.Model;

public class SolrSearchFacetsGeneratorTest {

    Collection<Page<FacetFieldEntry>> facetPages;

    private Map<String,List<FacetFieldEntry>> eresourcesPage;

    private FacetsGenerator generator;

    private Map<String, Object> model;

    private List<FacetFieldEntry> pageFieldFacet;

    private EresourceFacetService service;
    
   
    
    SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy;

    @Before
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
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 4, publicationTypes);
        this.generator.setFacet(facets);
        this.generator.setModel(model);
        Field field = new Field("publicationType");
        expect(this.service.facetByManyFields("skin", "", 5)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.get("publicationType")).andReturn(this.pageFieldFacet);
        expect(this.pageFieldFacet).andReturn(Arrays.asList(new FacetFieldEntry(field, "Required1", 100)));
        expect(this.service.facetByField("skin", "","publicationType",1000, 1, FacetSort.COUNT)).andReturn(this.eresourcesPage);
        replay(this.service, this.eresourcesPage);
        this.generator.generate();
        verify(this.service, this.eresourcesPage);
    }
    
    @Test
    public final void testNotEnoughtPublicationResult() throws Exception {
        Collection<String> facets = Arrays.asList("type", "publicationType");
        Collection<String> publicationTypes = Arrays.asList("Required1", "Required2");
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 4, publicationTypes);
        this.generator.setFacet(facets);
        this.generator.setModel(model);
        Field fieldType = new Field("type");
        Field fieldPublicationType = new Field("publicationType");
        List<FacetFieldEntry> resultFromSolr = new ArrayList<FacetFieldEntry>();
        resultFromSolr.add(new FacetFieldEntry(fieldType, "index", 10));
        resultFromSolr.add(new FacetFieldEntry(fieldPublicationType, "requiredIndex", 100));
        
        expect(this.service.facetByManyFields("skin", "", 5)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.get("type")).andReturn(this.pageFieldFacet);
        expect(this.eresourcesPage.get("publicationType")).andReturn(this.pageFieldFacet);
        
        //Second call to the facetService
        expect(this.service.facetByField("skin", "","publicationType", 1000, 1, FacetSort.COUNT)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.get("publicationType")).andReturn(this.pageFieldFacet);
        
        replay(this.service, this.eresourcesPage);
        this.generator.generate();
        verify(this.service, this.eresourcesPage);
    }
    
    
}
