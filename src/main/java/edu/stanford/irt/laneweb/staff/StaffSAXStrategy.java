package edu.stanford.irt.laneweb.staff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class StaffSAXStrategy implements SAXStrategy<Source> {

    private static final String CDATA = "CDATA";

    private static final String COLUMN_SEPARETOR = "\t";

    private static final String ID = "id";

    private static final String NAMESPACE = "http://lane.stanford.edu/staff/1.0";

    private static final String STAFF = "staff";

    private static final String STAFF_DIRECTORY = "staff-directory";

    @Override
    public void toSAX(final Source source, final XMLConsumer xmlConsumer) {
        List<String> lines = new ArrayList<>();
        String line = null;
        try (InputStream inputStream = source.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            this.toSAX(lines, xmlConsumer);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    private void toSAX(final List<String> lines, final XMLConsumer xmlConsumer) {
        String[] headers = lines.get(0).split(COLUMN_SEPARETOR);
        lines.remove(0);
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, STAFF_DIRECTORY);
            Collections.shuffle(lines, new Random());
            lines.stream().forEach((final String p) -> toSAX(headers, p, xmlConsumer));
            XMLUtils.endElement(xmlConsumer, NAMESPACE, STAFF_DIRECTORY);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void toSAX(final String[] headers, final String staff, final XMLConsumer xmlConsumer) {
        try {
            String[] staffData = staff.split(COLUMN_SEPARETOR);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", ID, ID, CDATA, staffData[0]);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, STAFF, atts);
            for (int i = 1; i < headers.length && i < staffData.length; i++) {
                String header = headers[i].toLowerCase().replace(" ", "-");
                XMLUtils.maybeCreateElement(xmlConsumer, NAMESPACE, header, staffData[i]);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, STAFF);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
