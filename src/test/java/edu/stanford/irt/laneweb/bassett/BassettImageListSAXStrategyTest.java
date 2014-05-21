package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class BassettImageListSAXStrategyTest {

    private List<BassettImage> collection;

    private BassettImage image;

    private BassettImageListSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.image = createMock(BassettImage.class);
        this.collection = Collections.singletonList(this.image);
        this.strategy = new BassettImageListSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.image.getBassettNumber()).andReturn("bn");
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getImage()).andReturn("image");
        expect(this.image.getDiagram()).andReturn("diagram");
        expect(this.image.getLatinLegend()).andReturn("legend");
        expect(this.image.getEngishLegend()).andReturn("legend").times(2);
        expect(this.image.getDescription()).andReturn("description").times(2);
        expect(this.image.getRegions()).andReturn(
                Arrays.asList(new String[] { "region1--subregion1", "region1--subregion2", "region2--subregion1" }));
        replay(this.image);
        this.strategy.toSAX(this.collection, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "BassettImageListSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.image);
    }

    @Test
    public void testToSAXNullDescription() throws SAXException, IOException {
        expect(this.image.getBassettNumber()).andReturn("bn");
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getImage()).andReturn("image");
        expect(this.image.getDiagram()).andReturn("diagram");
        expect(this.image.getLatinLegend()).andReturn("legend");
        expect(this.image.getEngishLegend()).andReturn("legend").times(2);
        expect(this.image.getDescription()).andReturn(null);
        expect(this.image.getRegions()).andReturn(Collections.singletonList("region"));
        replay(this.image);
        this.strategy.toSAX(this.collection, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "BassettImageListSAXStrategyTest-testToSAXNullDescription.xml"), this.xmlConsumer.getStringValue());
        verify(this.image);
    }

    @Test
    public void testToSAXNullLegend() throws SAXException, IOException {
        expect(this.image.getBassettNumber()).andReturn("bn");
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getImage()).andReturn("image");
        expect(this.image.getDiagram()).andReturn("diagram");
        expect(this.image.getLatinLegend()).andReturn("legend");
        expect(this.image.getEngishLegend()).andReturn(null);
        expect(this.image.getDescription()).andReturn("description").times(2);
        expect(this.image.getRegions()).andReturn(Collections.singletonList("region"));
        replay(this.image);
        this.strategy.toSAX(this.collection, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "BassettImageListSAXStrategyTest-testToSAXNullLegend.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.image);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer x = createMock(XMLConsumer.class);
        x.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.image, x);
        this.strategy.toSAX(this.collection, x);
        verify(this.image, x);
    }
}
