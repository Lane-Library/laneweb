package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.text.IsEqualCompressingWhiteSpace.equalToCompressingWhiteSpace;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.stanford.irt.laneweb.TestXMLConsumer;

public class EquipmentStatusTransformerTest {

    private EquipmentService service;

    private EquipmentStatusTransformer transformer;

    private TestXMLConsumer xmlConsumer;

    private XMLReader xmlReader;

    @Before
    public void setUp() throws SAXException {
        this.service = mock(EquipmentService.class);
        this.transformer = new EquipmentStatusTransformer(this.service);
        this.xmlConsumer = new TestXMLConsumer();
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.xmlReader = XMLReaderFactory.createXMLReader();
        this.xmlReader.setContentHandler(this.transformer);
        this.xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", this.transformer);
    }

    @Test
    public void testTransform() throws IOException, SAXException, SQLException {
        List<EquipmentStatus> status = new ArrayList<>();
        status.add(new EquipmentStatus("304254", "1"));
        status.add(new EquipmentStatus("296290", "10"));
        expect(this.service.getStatus("304254,296290")).andReturn(status);
        replay(this.service);
        this.xmlReader.parse(new InputSource(getClass().getResourceAsStream("equipment.html")));
        assertTrue(equalToCompressingWhiteSpace(
                this.xmlConsumer.getExpectedResult(this, "EquipmentStatusTransformerTest-test.xml"))
                        .matches(this.xmlConsumer.getStringValue()));
        verify(this.service);
    }
}
