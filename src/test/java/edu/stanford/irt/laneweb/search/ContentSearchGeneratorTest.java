package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ContentSearchGeneratorTest {

    private ContentResult contentResult;

    private ContentSearchGenerator generator;

    private MetaSearchManager manager;

    private MetaSearchManagerSource msms;

    private Result result;

    private SAXStrategy<PagingSearchResultSet> saxStrategy;

    private XMLConsumer xmlConsumer;

    private ScoreStrategy scoreStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.saxStrategy = createMock(SAXStrategy.class);
        this.scoreStrategy = createMock(ScoreStrategy.class);
        this.generator = new ContentSearchGenerator(this.saxStrategy, this.scoreStrategy);
        this.msms = createMock(MetaSearchManagerSource.class);
        this.manager = createMock(MetaSearchManager.class);
        expect(this.msms.getMetaSearchManager()).andReturn(this.manager);
        replay(this.msms);
        this.generator.setMetaSearchManagerSource(this.msms);
        verify(this.msms);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.generator.setConsumer(this.xmlConsumer);
        this.result = createMock(Result.class);
        this.contentResult = createMock(ContentResult.class);
    }

    @Test
    public void testDoGenerate() throws SAXException, IOException {
        expect(this.manager.search(isA(SimpleQuery.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        expect(this.result.getChildren()).andReturn(Collections.<Result> emptyList());
        this.saxStrategy.toSAX(isA(PagingSearchResultSet.class), eq(this.xmlConsumer));
        replay(this.manager, this.result, this.xmlConsumer, this.saxStrategy, this.scoreStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.manager, this.result, this.xmlConsumer, this.saxStrategy, this.scoreStrategy);
    }

    @Test
    public void testDoGenerateNullQuery() throws SAXException, IOException {
        this.saxStrategy.toSAX(isA(PagingSearchResultSet.class), eq(this.xmlConsumer));
        replay(this.manager, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.manager, this.xmlConsumer);
    }

    @Test
    public void testDoSearch() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        replay(this.manager);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        assertEquals(this.result, this.generator.doSearch());
        verify(this.manager);
    }

    @Test
    public void testDoSearchNullQuery() {
        replay(this.manager);
        assertNotNull(this.generator.doSearch());
        verify(this.manager);
    }

    @Test
    public void testGetContentResultList() {
        expect(this.result.getChildren()).andReturn(Collections.singletonList(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result }));
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getId()).andReturn("id_content");
        expect(this.result.getChildren()).andReturn(Collections.singletonList((Result) this.contentResult));
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(100);
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getContentId()).andReturn("contentId");
        replay(this.result, this.contentResult, this.saxStrategy, this.scoreStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        assertEquals(1, this.generator.getContentResultList(this.result).size());
        verify(this.result, this.contentResult, this.saxStrategy, this.scoreStrategy);
    }
}
