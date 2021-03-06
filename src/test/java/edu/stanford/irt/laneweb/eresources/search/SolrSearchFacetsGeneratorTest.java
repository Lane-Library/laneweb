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
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

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

    @Before
    public void setUp() {
        this.service = mock(SolrService.class);
        this.marshaller = mock(Marshaller.class);
        this.xmlConsumer = mock(XMLConsumer.class);
        Collection<String> publicationTypes = Arrays.asList("Required1", "Required2");
        FacetComparator facetComparator = new FacetComparator(publicationTypes);
        this.generator = new SolrSearchFacetsGenerator(this.service, this.marshaller, 10, 2,
                Arrays.asList("MeshSkip1", "MeshSkip2"), publicationTypes, facetComparator);
        this.model = new HashMap<>();
        this.facetpage = mock(FacetPage.class);
        this.pageFacetQueries = mock(Page.class);
        this.facetQueryEntry = mock(FacetQueryEntry.class);
        this.facetResultPages = mock(Collection.class);
    }

    @Test
    public final void testDoGenerateXMLConsumerBrowseFacetField() throws Exception {
        Iterator<FacetQueryEntry> it0 = mock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        Page<FacetFieldEntry> page2 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        Field field = mock(Field.class);
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

    @Test
    public final void testDoGenerateXMLConsumerBrowseFacetQuery() throws Exception {
        Iterator<FacetQueryEntry> facetQueryIterator = mock(Iterator.class);
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

    @Test
    public final void testDoGenerateXMLConsumerSearch() throws Exception {
        Iterator<FacetQueryEntry> it0 = mock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        Field field = mock(Field.class);
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

    @Test
    public final void testDoGenerateXMLConsumerSearchWithAddPrioritizedPublicationTypes() throws Exception {
        Iterator<FacetQueryEntry> it0 = mock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        Field field = mock(Field.class);
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
        expect(this.service.facetByField("query", "", "publicationType", 0, 1000, 1, FacetSort.COUNT))
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

    @Test
    public final void testDoGenerateXMLConsumerSearchWithRequestMoreMesh() throws Exception {
        Iterator<FacetQueryEntry> it0 = mock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        Field field = mock(Field.class);
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

    @Test
    public final void testDoGenerateXMLConsumerSearchWithRequestMoreTypes() throws Exception {
        Iterator<FacetQueryEntry> it0 = mock(Iterator.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        Field field = mock(Field.class);
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
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.hasContent()).andReturn(true);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getField()).andReturn(field);
        expect(field.getName()).andReturn("type");
        expect(ffe.getValue()).andReturn("Book");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("type2");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("type3");
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
        expect(this.service.facetByField("query", "", "type", 0, 1000, 1, FacetSort.COUNT)).andReturn(this.facetpage);
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
        expect(field.getName()).andReturn("type");
        expect(ffe.getValue()).andReturn("Book Digital");
        expect(ffe.getValueCount()).andReturn(new Long(10));
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("Book Print");
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
                "{publicationType=[Required1 = 10; enabled=false; url=publicationType%3A%22Required1%22, Required2 = 10; enabled=false; url=publicationType%3A%22Required2%22], type=[Book = 10; enabled=false; url=type%3A%22Book%22, Book Digital = 10; enabled=false; url=type%3A%22Book+Digital%22, Book Print = 10; enabled=false; url=type%3A%22Book+Print%22, type2 = 10; enabled=false; url=type%3A%22type2%22, type3 = 10; enabled=false; url=type%3A%22type3%22], mesh=[mesh1 = 10; enabled=false; url=mesh%3A%22mesh1%22, mesh2 = 10; enabled=false; url=mesh%3A%22mesh2%22, mesh3 = 10; enabled=false; url=mesh%3A%22mesh3%22]}",
                mapCapture.toString());
    }
}
