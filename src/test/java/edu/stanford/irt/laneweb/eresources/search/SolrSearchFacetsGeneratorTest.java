package edu.stanford.irt.laneweb.eresources.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.sax.SAXResult;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetComparator;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetService;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetsGenerator;
import edu.stanford.irt.laneweb.model.Model;

public class SolrSearchFacetsGeneratorTest {

    Collection<Page<FacetFieldEntry>> facetPages;

    private FacetPage<Eresource> eresourcesPage;

    private FacetQueryEntry facetQueryEntry;

    private FacetsGenerator generator;

    private Map<String, Object> model;

    //
    private Page<FacetFieldEntry> pageFieldFacet;

    //
    private FacetService service;

    SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy;

    //
    @Before
    public void setUp() {
        this.service = mock(FacetService.class);
        this.facetSAXStrategy = mock(SAXStrategy.class);
        this.pageFieldFacet = mock(Page.class);
        Collection<String> publicationTypes = Arrays.asList("Required1");
        this.generator = new FacetsGenerator(this.service, facetSAXStrategy, 4, publicationTypes);
        this.eresourcesPage = mock(FacetPage.class);
        this.facetQueryEntry = mock(FacetQueryEntry.class);
        this.facetPages = mock(Collection.class);
        this.model = new HashMap<String, Object>();
        this.model.put(Model.QUERY, "skin");
        this.generator.setModel(model);
        this.generator.setSelectedFacet(Arrays.asList("publicationType"));
    }

    @Test
    public final void testSimpleDoGenerate() throws Exception {
        SimpleField field = new SimpleField("publicationType");
        expect(this.service.facetByManyFields("skin", "", 5)).andReturn(this.eresourcesPage);
        expect(this.eresourcesPage.getFacetResultPage("publicationType")).andReturn(this.pageFieldFacet);
        expect(this.pageFieldFacet.getContent())
                .andReturn(Arrays.asList(new SimpleFacetFieldEntry(field, "Required1", 100)));
        replay(this.service, this.eresourcesPage, this.pageFieldFacet);
        this.generator.generate();
        verify(this.service, this.eresourcesPage, this.pageFieldFacet);
    }
    
//    @Test
//    public final void testNotEnoughtPublicationResult() throws Exception {
//        this.generator.setSelectedFacet(Arrays.asList("type","publicationType"));
//        SimpleField field = new SimpleField("type");
//        expect(this.service.facetByManyFields("skin", "", 5)).andReturn(this.eresourcesPage);
//        expect(this.eresourcesPage.getFacetResultPage("type")).andReturn(this.pageFieldFacet);
//        expect(this.pageFieldFacet.getContent()).andReturn(Arrays.asList(new SimpleFacetFieldEntry(field, "Required1", 100)));
//        replay(this.service, this.eresourcesPage, this.pageFieldFacet);
//        this.generator.generate();
//        verify(this.service, this.eresourcesPage, this.pageFieldFacet);
//    }
//    
    
}
