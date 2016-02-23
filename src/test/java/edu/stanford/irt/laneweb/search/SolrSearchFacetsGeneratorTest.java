package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
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
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.Eresource;
import edu.stanford.irt.laneweb.solr.Facet;
import edu.stanford.irt.laneweb.solr.SolrService;

public class SolrSearchFacetsGeneratorTest {

    Collection<Page<FacetFieldEntry>> facetResultPages;

    private FacetPage<Eresource> facetpage;

    private FacetQueryEntry facetQueryEntry;

    private SolrSearchFacetsGenerator generator;

    private Marshaller marshaller;

    private Map<String, Object> model;

    private Page<FacetQueryEntry> pageFacetQueries;

    private SolrService service;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(SolrService.class);
        this.marshaller = createMock(Marshaller.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.generator = new SolrSearchFacetsGenerator(this.service, this.marshaller);
        this.model = new HashMap<String, Object>();
        this.generator.setFacetsToShowBrowse(10);
        this.generator.setFacetsToShowSearch(2);
        this.generator.setMeshToIgnoreInSearch(Arrays.asList("MeshSkip1", "MeshSkip2"));
        this.generator.setRequiredPublicationTypes(Arrays.asList("Required1", "Required2"));
        this.facetpage = createMock(FacetPage.class);
        this.pageFacetQueries = createMock(Page.class);
        this.facetQueryEntry = createMock(FacetQueryEntry.class);
        this.facetResultPages = createMock(Collection.class);
    }

    @SuppressWarnings({ "boxing", "unchecked" })
    @Test
    public final void testDoGenerateXMLConsumerBrowseFacetField() throws Exception {
        Iterator<FacetQueryEntry> it0 = createMock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = createMock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = createMock(Iterator.class);
        Page<FacetFieldEntry> page1 = createMock(Page.class);
        Page<FacetFieldEntry> page2 = createMock(Page.class);
        FacetFieldEntry ffe = createMock(FacetFieldEntry.class);
        Field field = createMock(Field.class);
        Capture<Map<String, Collection<Facet>>> mapCapture = newCapture();
        Capture<SAXResult> saxResultCapture = newCapture();
        this.model.put(Model.FACET, "facet");
        this.model.put(Model.QUERY, "query");
        expect(this.service.facetByField("query", "", "facet", 0, 11, 1, FacetSort.COUNT)).andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(it0);
        expect(it0.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(this.facetResultPages);
        expect(this.facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(false);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page2);
        expect(page2.hasContent()).andReturn(true);
        expect(page2.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("fieldName");
        expect(ffe.getValue()).andReturn("value");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        //
        this.marshaller.marshal(capture(mapCapture), capture(saxResultCapture));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, page2, it2, ffe, field);
        this.generator.setModel(this.model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, page2, it2, ffe, field);
        assertEquals("{fieldName=[value = 10; enabled=false; url=fieldName%3A%22value%22]}", mapCapture.toString());
    }

    @SuppressWarnings({ "boxing", "unchecked" })
    @Test
    public final void testDoGenerateXMLConsumerBrowseFacetQuery() throws Exception {
        Iterator<FacetQueryEntry> facetQueryIterator = createMock(Iterator.class);
        Capture<Map<String, Collection<Facet>>> mapCapture = newCapture();
        Capture<SAXResult> saxResultCapture = newCapture();
        this.model.put(Model.FACET, "facet");
        this.model.put(Model.FACET_SORT, "index");
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.PAGE, "5");
        expect(this.service.facetByField("query", "", "facet", 4, 11, 1, FacetSort.INDEX)).andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(facetQueryIterator);
        expect(facetQueryIterator.hasNext()).andReturn(true);
        expect(facetQueryIterator.next()).andReturn(this.facetQueryEntry);
        expect(this.facetQueryEntry.getValue()).andReturn("field:value");
        expect(this.facetQueryEntry.getValueCount()).andReturn(new Long(1));
        expect(facetQueryIterator.hasNext()).andReturn(true);
        expect(facetQueryIterator.next()).andReturn(this.facetQueryEntry);
        expect(this.facetQueryEntry.getValue()).andReturn("field:value2");
        expect(this.facetQueryEntry.getValueCount()).andReturn(new Long(1));
        expect(facetQueryIterator.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(Collections.emptyList());
        //
        this.marshaller.marshal(capture(mapCapture), capture(saxResultCapture));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries,
                facetQueryIterator, this.facetQueryEntry);
        this.generator.setModel(this.model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries,
                facetQueryIterator, this.facetQueryEntry);
        assertEquals(
                "{field=[value = 1; enabled=false; url=field%3A%22value%22, value2 = 1; enabled=false; url=field%3A%22value2%22]}",
                mapCapture.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testDoGenerateXMLConsumerSearch() throws Exception {
        Iterator<FacetQueryEntry> it0 = createMock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = createMock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = createMock(Iterator.class);
        Page<FacetFieldEntry> page1 = createMock(Page.class);
        FacetFieldEntry ffe = createMock(FacetFieldEntry.class);
        Field field = createMock(Field.class);
        Capture<Map<String, Collection<Facet>>> mapCapture = newCapture();
        Capture<SAXResult> saxResultCapture = newCapture();
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.FACETS, "foo:\"bar\"::mesh:\"mesh1\"");
        expect(this.service.facetByManyFields("query", "foo:\"bar\"::mesh:\"mesh1\"", 3)).andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(it0);
        expect(it0.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(this.facetResultPages);
        expect(this.facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("publicationType");
        expect(ffe.getValue()).andReturn("Required1");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("Required2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("mesh");
        expect(ffe.getValue()).andReturn("mesh1");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("mesh2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("mesh3");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        //
        this.marshaller.marshal(capture(mapCapture), capture(saxResultCapture));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, it2, ffe, field);
        this.generator.setModel(this.model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, it2, ffe, field);
        assertEquals(
                "{publicationType=[Required1 = 10; enabled=false; url=foo%3A%22bar%22%3A%3Amesh%3A%22mesh1%22%3A%3ApublicationType%3A%22Required1%22, Required2 = 10; enabled=false; url=foo%3A%22bar%22%3A%3Amesh%3A%22mesh1%22%3A%3ApublicationType%3A%22Required2%22], mesh=[mesh1 = 10; enabled=true; url=foo%3A%22bar%22, mesh2 = 10; enabled=false; url=foo%3A%22bar%22%3A%3Amesh%3A%22mesh1%22%3A%3Amesh%3A%22mesh2%22, mesh3 = 10; enabled=false; url=foo%3A%22bar%22%3A%3Amesh%3A%22mesh1%22%3A%3Amesh%3A%22mesh3%22], foo=[bar = 0; enabled=true; url=mesh%3A%22mesh1%22]}",
                mapCapture.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testDoGenerateXMLConsumerSearchWithAddRequiredPublicationTypes() throws Exception {
        Iterator<FacetQueryEntry> it0 = createMock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = createMock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = createMock(Iterator.class);
        Page<FacetFieldEntry> page1 = createMock(Page.class);
        FacetFieldEntry ffe = createMock(FacetFieldEntry.class);
        Field field = createMock(Field.class);
        Capture<Map<String, Collection<Facet>>> mapCapture = newCapture();
        Capture<SAXResult> saxResultCapture = newCapture();
        this.model.put(Model.QUERY, "query");
        expect(this.service.facetByManyFields("query", "", 3)).andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(it0);
        expect(it0.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(this.facetResultPages);
        expect(this.facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("mesh");
        expect(ffe.getValue()).andReturn("bar1");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("bar2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("bar3");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        //
        expect(this.service.facetByField("query", "", "publicationType", 0, 1000, 0, FacetSort.COUNT))
                .andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(it0);
        expect(it0.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(this.facetResultPages);
        expect(this.facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("publicationType");
        expect(ffe.getValue()).andReturn("Required1");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("Required2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        //
        this.marshaller.marshal(capture(mapCapture), capture(saxResultCapture));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, it2, ffe, field);
        this.generator.setModel(this.model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, it2, ffe, field);
        assertEquals(
                "{mesh=[bar1 = 10; enabled=false; url=mesh%3A%22bar1%22, bar2 = 10; enabled=false; url=mesh%3A%22bar2%22, bar3 = 10; enabled=false; url=mesh%3A%22bar3%22], publicationType=[Required1 = 10; enabled=false; url=publicationType%3A%22Required1%22, Required2 = 10; enabled=false; url=publicationType%3A%22Required2%22]}",
                mapCapture.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testDoGenerateXMLConsumerSearchWithRequestMoreMesh() throws Exception {
        Iterator<FacetQueryEntry> it0 = createMock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = createMock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = createMock(Iterator.class);
        Page<FacetFieldEntry> page1 = createMock(Page.class);
        FacetFieldEntry ffe = createMock(FacetFieldEntry.class);
        Field field = createMock(Field.class);
        Capture<Map<String, Collection<Facet>>> mapCapture = newCapture();
        Capture<SAXResult> saxResultCapture = newCapture();
        this.model.put(Model.QUERY, "query");
        expect(this.service.facetByManyFields("query", "", 3)).andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(it0);
        expect(it0.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(this.facetResultPages);
        expect(this.facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("publicationType");
        expect(ffe.getValue()).andReturn("Required1");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("Required2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        //
        expect(this.service.facetByField("query", "", "mesh", 0, 6, 1, FacetSort.COUNT)).andReturn(this.facetpage);
        expect(this.facetpage.getFacetQueryResult()).andReturn(this.pageFacetQueries);
        expect(this.pageFacetQueries.iterator()).andReturn(it0);
        expect(it0.hasNext()).andReturn(false);
        //
        expect(this.facetpage.getFacetResultPages()).andReturn(this.facetResultPages);
        expect(this.facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("mesh");
        expect(ffe.getValue()).andReturn("Mesh1");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("Mesh2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        //
        this.marshaller.marshal(capture(mapCapture), capture(saxResultCapture));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, it2, ffe, field);
        this.generator.setModel(this.model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetpage, this.pageFacetQueries, it0,
                this.facetResultPages, it1, page1, it2, ffe, field);
        assertEquals(
                "{publicationType=[Required1 = 10; enabled=false; url=publicationType%3A%22Required1%22, Required2 = 10; enabled=false; url=publicationType%3A%22Required2%22], mesh=[Mesh1 = 10; enabled=false; url=mesh%3A%22Mesh1%22, Mesh2 = 10; enabled=false; url=mesh%3A%22Mesh2%22]}",
                mapCapture.toString());
    }
}
