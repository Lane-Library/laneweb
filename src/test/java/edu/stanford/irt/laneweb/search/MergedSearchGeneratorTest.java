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

    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.generator = new MergedSearchGenerator(this.collectionManager);
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
    public void testDoGenerate() throws SAXException {
        expect(this.collectionManager.search("query")).andReturn(Collections.singleton(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.manager.search(isA(Query.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        expect(this.result.getChildren()).andReturn(Collections.<Result> emptySet());
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/resources/1.0");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("resources"), eq("resources"),
                isA(Attributes.class));
        this.xmlConsumer
                .startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("query"), eq("query"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("query".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("query"), eq("query"));
        expect(this.eresource.getScore()).andReturn(0);
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("result"), eq("result"),
                isA(Attributes.class));
        expect(this.eresource.getId()).andReturn(0);
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("id"), eq("id"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '0' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("id"), eq("id"));
        expect(this.eresource.getRecordId()).andReturn(0);
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("recordId"), eq("recordId"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '0' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("recordId"), eq("recordId"));
        expect(this.eresource.getRecordType()).andReturn("type");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("recordType"), eq("recordType"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("type".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("recordType"), eq("recordType"));
        expect(this.eresource.getTitle()).andReturn("title");
        this.xmlConsumer
                .startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("title"), eq("title"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("title".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("title"), eq("title"));
        expect(this.eresource.getDescription()).andReturn("description");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("description"), eq("description"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("description".toCharArray()), eq(0), eq(11));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("description"), eq("description"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("versions"), eq("versions"),
                isA(Attributes.class));
        expect(this.eresource.getVersions()).andReturn(Collections.<Version> emptySet());
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("versions"), eq("versions"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("result"), eq("result"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("contentHitCounts"), eq("contentHitCounts"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("contentHitCounts"), eq("contentHitCounts"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("resources"), eq("resources"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
    }

    @Test
    public void testDoGenerateThrowException() throws SAXException {
        expect(this.collectionManager.search("query")).andReturn(Collections.singleton(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.manager.search(isA(Query.class), eq(20000L), eq(Collections.<String> emptyList()), eq(true))).andReturn(
                this.result);
        expect(this.result.getChildren()).andReturn(Collections.<Result> emptySet());
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        try {
            this.generator.doGenerate(this.xmlConsumer);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.collectionManager, this.xmlConsumer, this.eresource, this.manager, this.result);
    }
}
