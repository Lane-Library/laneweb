package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class MergedSearchGeneratorTest {

    private CollectionManager collectionManager;

    private Eresource eresource;

    private MergedSearchGenerator generator;

    private MetaSearchManager manager;

    private Result result;

    private XMLConsumer xmlConsumer;
    
    private SAXStrategy<PagingSearchResultSet> saxStrategy;

    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new MergedSearchGenerator(this.collectionManager, this.saxStrategy);
        MetaSearchManagerSource msms = createMock(MetaSearchManagerSource.class);
        this.manager = createMock(MetaSearchManager.class);
        expect(msms.getMetaSearchManager()).andReturn(this.manager);
        replay(msms);
        this.generator.setMetaSearchManagerSource(msms);
        verify(msms);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.eresource = createMock(Eresource.class);
        this.result = createMock(Result.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.collectionManager.search("query")).andReturn(Collections.singleton(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.manager.search(isA(Query.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        expect(this.result.getChildren()).andReturn(Collections.<Result> emptySet());
        this.saxStrategy.toSAX(isA(PagingSearchResultSet.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result, this.saxStrategy);
    }

    @Test
    public void testDoGenerateEmptyQuery() {
        this.saxStrategy.toSAX(isA(PagingSearchResultSet.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, ""));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
    }

    @Test
    public void testDoGenerateNullQuery() {
        this.saxStrategy.toSAX(isA(PagingSearchResultSet.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
    }
}
