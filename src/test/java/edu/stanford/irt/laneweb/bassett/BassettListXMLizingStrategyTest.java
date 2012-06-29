package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
import java.util.Collections;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BassettListXMLizingStrategyTest {

    private Collection<BassettEresource> collection;

    private BassettEresource image;

    private BassettListXMLizingStrategy list;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.image = createMock(BassettEresource.class);
        this.collection = Collections.singletonList(this.image);
        this.list = new BassettListXMLizingStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassetts"), eq("bassetts"),
                isA(Attributes.class));
        expect(this.image.getBassettNumber()).andReturn("bn");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett"), eq("bassett"),
                isA(Attributes.class));
        expect(this.image.getTitle()).andReturn("title");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("title"), eq("title"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("title".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("title"), eq("title"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_image"), eq("bassett_image"),
                isA(Attributes.class));
        expect(this.image.getImage()).andReturn("image");
        this.xmlConsumer.characters(aryEq("image".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_image"), eq("bassett_image"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("diagram_image"), eq("diagram_image"),
                isA(Attributes.class));
        expect(this.image.getDiagram()).andReturn("diagram");
        this.xmlConsumer.characters(aryEq("diagram".toCharArray()), eq(0), eq(7));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("diagram_image"), eq("diagram_image"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("legend_image"), eq("legend_image"),
                isA(Attributes.class));
        expect(this.image.getLatinLegend()).andReturn("legend");
        this.xmlConsumer.characters(aryEq("legend".toCharArray()), eq(0), eq(6));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("legend_image"), eq("legend_image"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("legend"), eq("legend"), isA(Attributes.class));
        expect(this.image.getEngishLegend()).andReturn("legend").times(2);
        this.xmlConsumer.characters(aryEq("legend".toCharArray()), eq(0), eq(6));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("legend"), eq("legend"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("description"), eq("description"),
                isA(Attributes.class));
        expect(this.image.getDescription()).andReturn("description").times(2);
        this.xmlConsumer.characters(aryEq("description".toCharArray()), eq(0), eq(11));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("description"), eq("description"));
        expect(this.image.getRegions()).andReturn(Collections.singletonList("region"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("regions"), eq("regions"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"), isA(Attributes.class));
        // this.xmlConsumer.characters(aryEq("region".toCharArray()), eq(0),
        // eq(6));
        // TODO: get coverage of region/subregion handling
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("regions"), eq("regions"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett"), eq("bassett"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassetts"), eq("bassetts"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.image, this.xmlConsumer);
        this.list.toSAX(this.collection, this.xmlConsumer);
        verify(this.image, this.xmlConsumer);
    }
}
