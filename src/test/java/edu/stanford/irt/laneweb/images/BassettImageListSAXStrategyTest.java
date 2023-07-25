package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;

import edu.stanford.irt.bassett.model.BassettImage;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;


public class BassettImageListSAXStrategyTest {

    private Page<BassettImage> facetPage;

    private BassettImage image;

    private BassettImageListSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.image = mock(BassettImage.class);
        this.facetPage = mock(Page.class);
        this.strategy = new BassettImageListSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.image.getBassettNumber()).andReturn("bn");
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getSource()).andReturn("image");
        expect(this.image.getDiagram()).andReturn("diagram");
        expect(this.image.getSubRegions()).andReturn( Collections.singletonList("sub region"));
        expect(this.image.getLatinLegend()).andReturn("legend");
        expect(this.image.getEnglishLegend()).andReturn("legend").times(2);
        expect(this.image.getDescription()).andReturn("description").times(2);
        expect(this.facetPage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.facetPage.getTotalElements()).andReturn(1L);
        expect(this.facetPage.getTotalPages()).andReturn(1).times(2);
        expect(this.facetPage.getNumber()).andReturn(1).times(2);
        expect(this.facetPage.getSize()).andReturn(1);
        expect(this.facetPage.getNumberOfElements()).andReturn(1);
        expect(this.facetPage.isFirst()).andReturn(true);
        expect(this.facetPage.isLast()).andReturn(true);
        replay(this.image, this.facetPage);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "BassettImageListSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.image);
    }

    @Test
    public void testToSAXNullDescription() throws SAXException, IOException {
        expect(this.image.getBassettNumber()).andReturn("bn");
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getSource()).andReturn("image");
        expect(this.image.getSubRegions()).andReturn( Collections.singletonList("sub region"));
        expect(this.image.getDiagram()).andReturn("diagram");
        expect(this.image.getLatinLegend()).andReturn("legend");
        expect(this.image.getDescription()).andReturn(null);
        expect(this.image.getEnglishLegend()).andReturn("legend").times(2);
        expect(this.facetPage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.facetPage.getTotalElements()).andReturn(1L);
        expect(this.facetPage.getTotalPages()).andReturn(1).times(2);
        expect(this.facetPage.getNumber()).andReturn(1).times(2);
        expect(this.facetPage.getSize()).andReturn(1);
        expect(this.facetPage.getNumberOfElements()).andReturn(1);
        expect(this.facetPage.isFirst()).andReturn(true);
        expect(this.facetPage.isLast()).andReturn(true);
        replay(this.image, this.facetPage);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this,
                        "BassettImageListSAXStrategyTest-testToSAXNullDescription.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.image);
    }

    @Test
    public void testToSAXNullLegend() throws SAXException, IOException {
        expect(this.image.getBassettNumber()).andReturn("bn");
        expect(this.image.getTitle()).andReturn("title");
        expect(this.image.getSource()).andReturn("image");
        expect(this.image.getDiagram()).andReturn("diagram");
        expect(this.image.getSubRegions()).andReturn( Collections.singletonList("sub region"));
        expect(this.image.getLatinLegend()).andReturn("legend");
        expect(this.image.getEnglishLegend()).andReturn(null);
        expect(this.image.getDescription()).andReturn("description").times(2);
        expect(this.facetPage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.facetPage.getTotalElements()).andReturn(1L);
        expect(this.facetPage.getTotalPages()).andReturn(1).times(2);
        expect(this.facetPage.getNumber()).andReturn(1).times(2);
        expect(this.facetPage.getSize()).andReturn(1);
        expect(this.facetPage.getNumberOfElements()).andReturn(1);
        expect(this.facetPage.isFirst()).andReturn(true);
        expect(this.facetPage.isLast()).andReturn(true);
        replay(this.image, this.facetPage);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "BassettImageListSAXStrategyTest-testToSAXNullLegend.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.image);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer x = mock(XMLConsumer.class);
        x.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.image, x);
        this.strategy.toSAX(this.facetPage, x);
        verify(this.image, x);
    }
}
