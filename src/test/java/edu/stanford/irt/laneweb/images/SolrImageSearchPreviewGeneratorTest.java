package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import javax.xml.transform.sax.SAXResult;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchPreviewGeneratorTest {

    private FacetPage<Image> facetPage;

    private SolrImageSearchPreviewGenerator generator;

    private Marshaller marshaller;

    private Page<FacetFieldEntry> page;

    private SolrImageService service;

    private FacetFieldEntry value;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.service = mock(SolrImageService.class);
        this.marshaller = mock(Marshaller.class);
        this.generator = new SolrImageSearchPreviewGenerator(this.marshaller, this.service);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.facetPage = mock(FacetPage.class);
        this.page = mock(Page.class);
        this.value = mock(FacetFieldEntry.class);
    }

    @Test
    public final void testDoGenerateXMLConsumer() throws Exception {
        expect(this.service.facetOnCopyright("query")).andReturn(this.facetPage);
        expect(this.facetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.value));
        expect(this.value.getValueCount()).andReturn((long) 0);
        this.marshaller.marshal(eq(Collections.EMPTY_LIST), isA(SAXResult.class));
        replay(this.marshaller, this.service, this.xmlConsumer, this.facetPage, this.page, this.value);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller, this.service, this.xmlConsumer, this.facetPage, this.page, this.value);
    }
}
