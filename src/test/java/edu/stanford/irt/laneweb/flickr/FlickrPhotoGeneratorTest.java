package edu.stanford.irt.laneweb.flickr;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class FlickrPhotoGeneratorTest {

    private FlickrPhotoGenerator generator;

    private FlickrPhotoListService service;

    private SAXStrategy<List<FlickrPhoto>> strategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.service = mock(FlickrPhotoListService.class);
        this.strategy = mock(SAXStrategy.class);
        this.generator = new FlickrPhotoGenerator(this.service, this.strategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() {
        expect(this.service.getRandomPhotos(12)).andReturn(Collections.emptyList());
        this.strategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.service, this.strategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.strategy, this.xmlConsumer);
    }
}
