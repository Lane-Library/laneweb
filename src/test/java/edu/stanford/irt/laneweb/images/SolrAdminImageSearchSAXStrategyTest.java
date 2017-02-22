package edu.stanford.irt.laneweb.images;

import static org.junit.Assert.*;

import java.io.IOException;

import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.solr.Image;


public class SolrAdminImageSearchSAXStrategyTest {
    
    private  SolrAdminImageSearchSAXStrategy strategy;
    private TestXMLConsumer xmlConsumer;
    private Image image;

    @Before
    public void setUp() {
        this.strategy = new  SolrAdminImageSearchSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.image = createMock(Image.class);
    }

    @Test
    public void testGenerateImages() throws SAXException, IOException {
        expect(this.image.isEnable()).andReturn(true);
        expect(this.image.getId()).andReturn("id").times(2);
        expect(this.image.getPageUrl()).andReturn("url");
        expect(this.image.getThumbnailSrc()).andReturn("src");
        replay(this.image);
        this.xmlConsumer.startDocument();
        this.strategy.generateImages(this.xmlConsumer, this.image, 1);
        this.xmlConsumer.endDocument();
        verify(this.image);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrAdminImageSearchSAXStrategyTest-generateImages.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testGenerateImagesDisabled() throws SAXException, IOException {
        expect(this.image.isEnable()).andReturn(false);
        expect(this.image.getId()).andReturn("id").times(2);
        expect(this.image.getPageUrl()).andReturn("url");
        expect(this.image.getThumbnailSrc()).andReturn("src");
        replay(this.image);
        this.xmlConsumer.startDocument();
        this.strategy.generateImages(this.xmlConsumer, this.image, 1);
        this.xmlConsumer.endDocument();
        verify(this.image);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrAdminImageSearchSAXStrategyTest-generateImagesDisabled.xml"),
                this.xmlConsumer.getStringValue());
    }
}
