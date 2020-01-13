package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class GrandRoundsGeneratorTest {

    private GrandRoundsGenerator generator;

    private Map<String, String> parameters;

    private Presentation presentation;

    private SAXStrategy<Presentation> presentationSAXStrategy;

    private GrandRoundsService service;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.service = mock(GrandRoundsService.class);
        this.presentationSAXStrategy = mock(SAXStrategy.class);
        this.generator = new GrandRoundsGenerator(this.service, this.presentationSAXStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.parameters = new HashMap<>();
        this.presentation = mock(Presentation.class);
    }

    @Test
    public void testDoGenerate() throws IOException {
        this.parameters.put("department", "department");
        this.parameters.put("year", "year");
        this.generator.setParameters(this.parameters);
        expect(this.service.getByYear("department", "year")).andReturn(Collections.singletonList(this.presentation));
        this.presentationSAXStrategy.toSAX(this.presentation, this.xmlConsumer);
        replay(this.service, this.presentation, this.presentationSAXStrategy);
        this.generator.doGenerate(this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "GrandRoundsGeneratorTest-testDoGenerate.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.service, this.presentation, this.presentationSAXStrategy);
    }

    @Test
    public void testDoGenerateRecent() throws IOException {
        this.parameters.put("department", "department");
        this.parameters.put("limit", "5");
        this.generator.setParameters(this.parameters);
        expect(this.service.getRecent("department", "5")).andReturn(Collections.singletonList(this.presentation));
        this.presentationSAXStrategy.toSAX(this.presentation, this.xmlConsumer);
        replay(this.service, this.presentation, this.presentationSAXStrategy);
        this.generator.doGenerate(this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "GrandRoundsGeneratorTest-testDoGenerate.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.service, this.presentation, this.presentationSAXStrategy);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateThrowsException() throws IOException, SAXException {
        XMLConsumer mockXMLConsumer = mock(XMLConsumer.class);
        this.parameters.put("department", "department");
        this.parameters.put("year", "year");
        this.generator.setParameters(this.parameters);
        expect(this.service.getByYear("department", "year")).andReturn(Collections.singletonList(this.presentation));
        expect(this.presentation.getPresenterList())
                .andReturn(Arrays.asList(new String[] { "presenter1", "presenter2" }));
        mockXMLConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.service, this.presentation, this.presentationSAXStrategy, mockXMLConsumer);
        this.generator.doGenerate(mockXMLConsumer);
    }
}
