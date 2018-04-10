package edu.stanford.irt.laneweb.hours;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.libraryhours.Hours;

public class HoursListSAXStrategy implements SAXStrategy<List<List<Hours>>> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d");

    private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER = DateTimeFormatter.ofPattern("EEEE");

    private static final DateTimeFormatter HOURS_FORMATTER = DateTimeFormatter.ofPattern("h a");

    private static final String NS = "http://lane.stanford.edu/hours/1.0";

    private static void hoursListToSAX(final List<Hours> hours, final XMLConsumer xmlConsumer) {
        try {
            XMLUtils.startElement(xmlConsumer, NS, "calendar");
            hours.stream().forEach((final Hours h) -> hoursToSAX(h, xmlConsumer));
            XMLUtils.endElement(xmlConsumer, NS, "calendar");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private static void hoursToSAX(final Hours hours, final XMLConsumer xmlConsumer) {
        LocalDate date = hours.getDate();
        try {
            XMLUtils.startElement(xmlConsumer, NS, "day");
            XMLUtils.startElement(xmlConsumer, NS, "label");
            XMLUtils.data(xmlConsumer, date.format(DAY_OF_WEEK_FORMATTER));
            XMLUtils.endElement(xmlConsumer, NS, "label");
            XMLUtils.startElement(xmlConsumer, NS, "date");
            XMLUtils.data(xmlConsumer, date.format(DATE_FORMATTER));
            XMLUtils.endElement(xmlConsumer, NS, "date");
            XMLUtils.startElement(xmlConsumer, NS, "description");
            StringBuilder sb = new StringBuilder();
            if (hours.isClosed()) {
                sb.append("Closed");
            } else {
                sb.append(hours.getOpen().toLocalTime().format(HOURS_FORMATTER).toLowerCase(Locale.US)).append(" - ")
                        .append(hours.getClose().toLocalTime().format(HOURS_FORMATTER).toLowerCase(Locale.US));
            }
            XMLUtils.data(xmlConsumer, sb.toString());
            XMLUtils.endElement(xmlConsumer, NS, "description");
            XMLUtils.endElement(xmlConsumer, NS, "day");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public void toSAX(final List<List<Hours>> hours, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NS);
            xmlConsumer.startPrefixMapping("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation", "xsi:schemaLocation",
                    "CDATA", "http://lane.stanford.edu/hours/1.0 hours.xsd");
            XMLUtils.startElement(xmlConsumer, NS, "calendars", atts);
            hours.stream().forEach((final List<Hours> hl) -> hoursListToSAX(hl, xmlConsumer));
            XMLUtils.endElement(xmlConsumer, NS, "calendars");
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
