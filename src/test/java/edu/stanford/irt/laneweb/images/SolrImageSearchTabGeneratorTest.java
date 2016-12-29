package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;

import javax.xml.transform.sax.SAXResult;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.images.SolrImageSearchTabGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchTabGeneratorTest {

    private FacetPage<Image> facetPage;

    private SolrImageSearchTabGenerator generator;

    private Marshaller marshaller;

    private Page<FacetFieldEntry> page;

    private SolrImageService service;

    private FacetFieldEntry value;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(SolrImageService.class);
        this.marshaller = createMock(Marshaller.class);
        this.generator = new SolrImageSearchTabGenerator(this.service, this.marshaller);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.facetPage = createMock(FacetPage.class);
        this.page = createMock(Page.class);
        this.value = createMock(FacetFieldEntry.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws IOException {
        expect(this.service.facetOnCopyright("query")).andReturn(this.facetPage);
        expect(this.facetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.value));
        expect(this.value.getValue()).andReturn("value");
        expect(this.value.getValueCount()).andReturn(1L);
        this.marshaller.marshal(eq(Collections.singletonMap("value", "1")), isA(SAXResult.class));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetPage, this.page, this.value);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetPage, this.page, this.value);
    }
}
