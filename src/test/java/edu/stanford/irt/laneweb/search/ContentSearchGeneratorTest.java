package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ContentSearchGeneratorTest {

    private ContentSearchGenerator generator;

    private MetaSearchManager manager;

    private Map<String, Object> model;

    private MetaSearchManagerSource msms;

    private Parameters parameters;

    private Result result;

    private XMLConsumer xmlConsumer;

    private ContentResult contentResult;

    @Before
    public void setUp() throws Exception {
        this.generator = new ContentSearchGenerator();
        this.msms = createMock(MetaSearchManagerSource.class);
        this.manager = createMock(MetaSearchManager.class);
        expect(this.msms.getMetaSearchManager()).andReturn(this.manager);
        replay(this.msms);
        this.generator.setMetaSearchManagerSource(this.msms);
        verify(this.msms);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.generator.setConsumer(this.xmlConsumer);
        this.model = Collections.<String, Object> singletonMap(Model.QUERY, "query");
        this.parameters = createMock(Parameters.class);
        expect(this.parameters.getParameter(Model.TIMEOUT, null)).andReturn(null);
        expect(this.parameters.getParameter(Model.ENGINES, null)).andReturn(null);
        replay(this.parameters);
        this.generator.setup(null, this.model, null, this.parameters);
        verify(this.parameters);
        this.result = createMock(Result.class);
        this.contentResult = createMock(ContentResult.class);
    }

    @Test
    public void testDoSearch() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        replay(this.manager);
        assertEquals(this.result, this.generator.doSearch());
        verify(this.manager);
    }

    @Test
    public void testGenerate() throws SAXException, IOException {
        expect(this.manager.search(isA(SimpleQuery.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        expect(this.result.getChildren()).andReturn(Collections.<Result> emptyList());
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(3);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(3);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.manager, this.result, this.xmlConsumer);
        this.generator.generate();
        verify(this.manager, this.result, this.xmlConsumer);
    }

    @Test
    public void testGetContentResultList() {
        expect(this.result.getChildren()).andReturn(Collections.singletonList(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[]{this.result, this.result}));
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getId()).andReturn("id_content");
        expect(this.result.getChildren()).andReturn(Collections.singletonList((Result)this.contentResult));
        //TODO: should't have to get these values this many times . . .
        expect(this.contentResult.getTitle()).andReturn("title").times(4);
        expect(this.contentResult.getId()).andReturn("id");
        expect(this.contentResult.getDescription()).andReturn("description").times(2);
        expect(this.contentResult.getPublicationDate()).andReturn("publication date").times(2);
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getURL()).andReturn("url");
        expect(this.contentResult.getURL()).andReturn("url").times(2);
        replay(this.result, this.contentResult);
        assertEquals(1, this.generator.getContentResultList(this.result).size());
        verify(this.result, this.contentResult);
    }
}
