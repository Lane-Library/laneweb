package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;

public class EresourcesSearchGeneratorTest {

    private CollectionManager collectionManager;

    private EresourcesSearchGenerator generator;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.generator = new EresourcesSearchGenerator(this.collectionManager);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() throws SAXException {
        expect(this.collectionManager.search("query")).andReturn(Collections.<Eresource> emptyList());
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/resources/1.0");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("resources"), eq("resources"),
                isA(Attributes.class));
        this.xmlConsumer
                .startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("query"), eq("query"), isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("query"), eq("query"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/resources/1.0"), eq("contentHitCounts"), eq("contentHitCounts"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("contentHitCounts"), eq("contentHitCounts"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/resources/1.0"), eq("resources"), eq("resources"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.collectionManager);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.xmlConsumer, this.collectionManager);
    }

    @Test
    public void testGetEresourceList() {
        expect(this.collectionManager.search("query")).andReturn(Collections.<Eresource> emptyList());
        replay(this.collectionManager);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.getEresourceList();
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListNullQuery() {
        replay(this.collectionManager);
        assertEquals(0, this.generator.getEresourceList().size());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListTypeParameter() {
        Eresource eresource = createMock(Eresource.class);
        expect(eresource.getTitle()).andReturn("title");
        expect(this.collectionManager.searchType("type", "query")).andReturn(Collections.<Eresource> singletonList(eresource));
        replay(this.collectionManager, eresource);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        assertEquals(1, this.generator.getEresourceList().size());
        verify(this.collectionManager, eresource);
    }
}
