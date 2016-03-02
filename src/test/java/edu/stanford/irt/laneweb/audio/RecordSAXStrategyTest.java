package edu.stanford.irt.laneweb.audio;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.Record.Field;
import edu.stanford.lane.catalog.Record.Subfield;

public class RecordSAXStrategyTest {

    private static final String CONTROLFIELD = "controlfield";

    private static final String DATAFIELD = "datafield";

    private static final String MARC21 = "http://www.loc.gov/MARC21/slim";

    private static final String RECORD = "record";

    private static final String SUBFIELD = "subfield";

    private Field field;

    private Record record;

    private RecordSAXStrategy strategy;

    private Subfield subfield;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new RecordSAXStrategy();
        this.record = createMock(Record.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.field = createMock(Field.class);
        this.subfield = createMock(Subfield.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        this.xmlConsumer.startElement(eq(MARC21), eq(RECORD), eq(RECORD), isA(Attributes.class));
        expect(this.record.getFields()).andReturn(Arrays.asList(new Field[] { this.field, this.field }));
        expect(this.field.getTag()).andReturn("001");
        expect(this.field.getData()).andReturn("1");
        this.xmlConsumer.startElement(eq(MARC21), eq(CONTROLFIELD), eq(CONTROLFIELD), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '1' }), eq(0), eq(1));
        this.xmlConsumer.endElement(MARC21, CONTROLFIELD, CONTROLFIELD);
        expect(this.field.getTag()).andReturn("245");
        expect(this.field.getIndicator1()).andReturn(' ');
        expect(this.field.getIndicator2()).andReturn(' ');
        this.xmlConsumer.startElement(eq(MARC21), eq(DATAFIELD), eq(DATAFIELD), isA(Attributes.class));
        expect(this.field.getSubfields()).andReturn(Collections.singletonList(this.subfield));
        expect(this.subfield.getCode()).andReturn('a');
        expect(this.subfield.getData()).andReturn("A");
        this.xmlConsumer.startElement(eq(MARC21), eq(SUBFIELD), eq(SUBFIELD), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { 'A' }), eq(0), eq(1));
        this.xmlConsumer.endElement(MARC21, SUBFIELD, SUBFIELD);
        this.xmlConsumer.endElement(MARC21, DATAFIELD, DATAFIELD);
        this.xmlConsumer.endElement(MARC21, RECORD, RECORD);
        replay(this.record, this.xmlConsumer, this.field, this.subfield);
        this.strategy.toSAX(this.record, this.xmlConsumer);
        verify(this.record, this.xmlConsumer, this.field, this.subfield);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXFieldThrowsSAXException() throws SAXException {
        this.xmlConsumer.startElement(eq(MARC21), eq(RECORD), eq(RECORD), isA(Attributes.class));
        expect(this.record.getFields()).andReturn(Arrays.asList(new Field[] { this.field, this.field }));
        expect(this.field.getTag()).andReturn("001");
        expect(this.field.getData()).andReturn("1");
        this.xmlConsumer.startElement(eq(MARC21), eq(CONTROLFIELD), eq(CONTROLFIELD), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.record, this.xmlConsumer, this.field);
        this.strategy.toSAX(this.record, this.xmlConsumer);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXRecordThrowsSAXException() throws SAXException {
        this.xmlConsumer.startElement(eq(MARC21), eq(RECORD), eq(RECORD), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.record, this.xmlConsumer);
        this.strategy.toSAX(this.record, this.xmlConsumer);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXSubfieldThrowsSAXException() throws SAXException {
        this.xmlConsumer.startElement(eq(MARC21), eq(RECORD), eq(RECORD), isA(Attributes.class));
        expect(this.record.getFields()).andReturn(Arrays.asList(new Field[] { this.field, this.field }));
        expect(this.field.getTag()).andReturn("001");
        expect(this.field.getData()).andReturn("1");
        this.xmlConsumer.startElement(eq(MARC21), eq(CONTROLFIELD), eq(CONTROLFIELD), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '1' }), eq(0), eq(1));
        this.xmlConsumer.endElement(MARC21, CONTROLFIELD, CONTROLFIELD);
        expect(this.field.getTag()).andReturn("245");
        expect(this.field.getIndicator1()).andReturn(' ');
        expect(this.field.getIndicator2()).andReturn(' ');
        this.xmlConsumer.startElement(eq(MARC21), eq(DATAFIELD), eq(DATAFIELD), isA(Attributes.class));
        expect(this.field.getSubfields()).andReturn(Collections.singletonList(this.subfield));
        expect(this.subfield.getCode()).andReturn('a');
        expect(this.subfield.getData()).andReturn("A");
        this.xmlConsumer.startElement(eq(MARC21), eq(SUBFIELD), eq(SUBFIELD), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.record, this.xmlConsumer, this.field, this.subfield);
        this.strategy.toSAX(this.record, this.xmlConsumer);
    }
}
