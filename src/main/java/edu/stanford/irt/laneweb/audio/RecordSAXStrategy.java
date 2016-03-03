package edu.stanford.irt.laneweb.audio;

import java.util.regex.Pattern;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.Record.Field;
import edu.stanford.lane.catalog.Record.Subfield;

public class RecordSAXStrategy implements SAXStrategy<Record> {

    private static final String CDATA = "CDATA";

    private static final String CODE = "code";

    private static final Pattern CONTROL_FIELD_TAG = Pattern.compile("^00\\d$");

    private static final String CONTROLFIELD = "controlfield";

    private static final String DATAFIELD = "datafield";

    private static final String EMPTY = "";

    private static final String IND1 = "ind1";

    private static final String IND2 = "ind2";

    private static final String MARC21 = "http://www.loc.gov/MARC21/slim";

    private static final String RECORD = "record";

    private static final String SUBFIELD = "subfield";

    private static final String TAG = "tag";

    private static void fieldToSAX(final Field field, final XMLConsumer xmlConsumer) {
        String tag = field.getTag();
        boolean isControlField = CONTROL_FIELD_TAG.matcher(tag).matches();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, TAG, TAG, CDATA, tag);
        try {
            if (isControlField) {
                XMLUtils.createElement(xmlConsumer, MARC21, CONTROLFIELD, atts, field.getData());
            } else {
                atts.addAttribute(EMPTY, IND1, IND1, CDATA, Character.toString(field.getIndicator1()));
                atts.addAttribute(EMPTY, IND2, IND2, CDATA, Character.toString(field.getIndicator2()));
                XMLUtils.startElement(xmlConsumer, MARC21, DATAFIELD, atts);
                field.getSubfields().stream().forEach(s -> subfieldToSAX(s, xmlConsumer));
                XMLUtils.endElement(xmlConsumer, MARC21, DATAFIELD);
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private static void subfieldToSAX(final Subfield subfield, final XMLConsumer xmlConsumer) {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CODE, CODE, CDATA, Character.toString(subfield.getCode()));
        try {
            XMLUtils.createElement(xmlConsumer, MARC21, SUBFIELD, atts, subfield.getData());
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public void toSAX(final Record record, final XMLConsumer xmlConsumer) {
        try {
            XMLUtils.startElement(xmlConsumer, MARC21, RECORD);
            record.getFields().stream().forEach(f -> fieldToSAX(f, xmlConsumer));
            XMLUtils.endElement(xmlConsumer, MARC21, RECORD);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
