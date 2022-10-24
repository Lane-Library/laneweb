package edu.stanford.irt.laneweb.eresources.search;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;

public class FacetStrategyTest {

    private FacetSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    Map<String, Collection<FacetFieldEntry>> solrResult;

    @Before
    public void setUp() {
        solrResult = new HashedMap<String, Collection<FacetFieldEntry>>();
        Collection<FacetFieldEntry> typeResult = new ArrayList<FacetFieldEntry>();
        SimpleField fieldType = new SimpleField("type");
        typeResult.add(new SimpleFacetFieldEntry(fieldType, "index", 10));
        solrResult.put(fieldType.getName(), typeResult);
        Collection<FacetFieldEntry> publicationTypeResult = new ArrayList<FacetFieldEntry>();
        SimpleField fieldPublicationType = new SimpleField("publicationType");
        publicationTypeResult.add(new SimpleFacetFieldEntry(fieldPublicationType, "requiredIndex", 100));
        publicationTypeResult.add(new SimpleFacetFieldEntry(fieldPublicationType, "requiredIndex2", 10));
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
