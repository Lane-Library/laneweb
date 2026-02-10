package edu.stanford.irt.laneweb.resource;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class BannerResourceOutageGeneratorTest {

    private BannerResourceOutageService service;

    private SAXParser saxParser;

    private BannerResourceOutageGenerator generator;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.service = mock(BannerResourceOutageService.class);
        this.saxParser = mock(SAXParser.class);
        this.generator = new BannerResourceOutageGenerator(mock(Marshaller.class), this.saxParser, this.service);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateParsesServiceHtml() throws Exception {
        ByteArrayInputStream outages = new ByteArrayInputStream("<outages/>".getBytes(StandardCharsets.UTF_8));
        expect(this.service.getHtmlResourceOutages()).andReturn(outages);

        Capture<Source> sourceCapture = Capture.newInstance();
        this.saxParser.parse(capture(sourceCapture), same(this.xmlConsumer));

        replay(this.service, this.saxParser, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxParser, this.xmlConsumer);

        Source source = sourceCapture.getValue();
        assertNotNull(source);
        assertTrue(source.exists());
        assertSame(outages, source.getInputStream());
        assertNull(source.getURI());
    }
}
