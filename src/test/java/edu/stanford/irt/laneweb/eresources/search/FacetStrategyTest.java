package edu.stanford.irt.laneweb.eresources.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.Field;

public class FacetStrategyTest {

    private FacetSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    Map<String, Collection<FacetFieldEntry>> solrResult;

    @BeforeEach
    public void setUp() {
        solrResult = new HashedMap<String, Collection<FacetFieldEntry>>();
        Collection<FacetFieldEntry> typeResult = new ArrayList<FacetFieldEntry>();
        Field fieldType = new Field("type");
        typeResult.add(new FacetFieldEntry(fieldType, "index", 10));
        solrResult.put(fieldType.getName(), typeResult);
        Collection<FacetFieldEntry> publicationTypeResult = new ArrayList<FacetFieldEntry>();
        Field fieldPublicationType = new Field("publicationType");
        publicationTypeResult.add(new FacetFieldEntry(fieldPublicationType, "requiredIndex", 100));
        publicationTypeResult.add(new FacetFieldEntry(fieldPublicationType, "requiredIndex2", 10));
        solrResult.put(fieldPublicationType.getName(), publicationTypeResult);
        Collection<String> facets = Arrays.asList("type", "publicationType", "test");
        this.strategy = new FacetSAXStrategy(facets);
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException, SAXException, URISyntaxException {
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.solrResult, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "FacetSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }
}
