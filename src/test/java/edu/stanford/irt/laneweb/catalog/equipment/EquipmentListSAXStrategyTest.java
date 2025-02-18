package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class EquipmentListSAXStrategyTest {

    private Equipment equipment = mock(Equipment.class);

    private EquipmentListSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.saxStrategy = new EquipmentListSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException {
        expect(this.equipment.getBibID()).andReturn("bibid");
        expect(this.equipment.getTitle()).andReturn("Android Thing");
        expect(this.equipment.getNote()).andReturn("Checkout for 2 hours");
        expect(this.equipment.getCount()).andReturn("4");
        replay(this.equipment);
        this.saxStrategy.toSAX(Collections.singletonList(this.equipment), this.xmlConsumer);
        verify(this.equipment);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EquipmentListSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = mock(XMLConsumer.class);
        mock.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(mock);
        assertThrows(LanewebException.class, () -> {
            this.saxStrategy.toSAX(Collections.singletonList(this.equipment), mock);
        });

    }
}
