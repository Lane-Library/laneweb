package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
// import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;

import edu.stanford.irt.bassett.model.BassettImage;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class BassettImageListSAXStrategyTest {

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private Page<BassettImage> facetPage;

    private BassettImage image;

    private BassettImageListSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.image = new BassettImage();
        this.image.setBassettNumber("bn");
        this.image.setTitle("title");
        this.image.setSrc("src.L.jpg");
        this.image.setSubRegions(Collections.singletonList("sub region"));
        this.image.setEnglishLegend("legend");
        this.image.setDescription("description");
        this.facetPage = mock(Page.class);
        this.strategy = new BassettImageListSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.facetPage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.facetPage.getTotalElements()).andReturn(1L);
        expect(this.facetPage.getTotalPages()).andReturn(1).times(2);
        expect(this.facetPage.getNumber()).andReturn(1).times(2);
        expect(this.facetPage.getSize()).andReturn(1);
        expect(this.facetPage.getNumberOfElements()).andReturn(1);
        expect(this.facetPage.isFirst()).andReturn(true);
        expect(this.facetPage.isLast()).andReturn(true);
        replay(this.facetPage);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "BassettImageListSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());

    }

    @Test
    public void testToSAXNullDescription() throws SAXException, IOException {
        this.image.setDescription(null);
        expect(this.facetPage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.facetPage.getTotalElements()).andReturn(1L);
        expect(this.facetPage.getTotalPages()).andReturn(1).times(2);
        expect(this.facetPage.getNumber()).andReturn(1).times(2);
        expect(this.facetPage.getSize()).andReturn(1);
        expect(this.facetPage.getNumberOfElements()).andReturn(1);
        expect(this.facetPage.isFirst()).andReturn(true);
        expect(this.facetPage.isLast()).andReturn(true);
        replay(this.facetPage);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this,
                        "BassettImageListSAXStrategyTest-testToSAXNullDescription.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullLegend() throws SAXException, IOException {
        expect(this.facetPage.getContent()).andReturn(Collections.singletonList(this.image));
        expect(this.facetPage.getTotalElements()).andReturn(1L);
        expect(this.facetPage.getTotalPages()).andReturn(1).times(2);
        expect(this.facetPage.getNumber()).andReturn(1).times(2);
        expect(this.facetPage.getSize()).andReturn(1);
        expect(this.facetPage.getNumberOfElements()).andReturn(1);
        expect(this.facetPage.isFirst()).andReturn(true);
        expect(this.facetPage.isLast()).andReturn(true);
        replay(this.facetPage);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "BassettImageListSAXStrategyTest-testToSAXNullLegend.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer x = mock(XMLConsumer.class);
        x.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(x);
        assertThrows(LanewebException.class, () -> {
            this.strategy.toSAX(this.facetPage, x);
            verify(x);
        });
    }
}
