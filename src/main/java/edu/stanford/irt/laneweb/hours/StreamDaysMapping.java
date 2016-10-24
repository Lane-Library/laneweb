package edu.stanford.irt.laneweb.hours;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class StreamDaysMapping extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;

    private static final Pattern STRIPPABLE_MINUTES_PATTERN = Pattern.compile(":00");

    private static final String TAG_CALENDAR = "calendar";

    private static final String TAG_DATE = "date";

    private static final String TAG_DAY = "day";

    private static final String TAG_DESCRIPTION = "description";

    private static final String TAG_LABEL = "label";

    public StreamDaysMapping(final InputStream inputStream) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse(inputStream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new LanewebException(e);
        }
        NodeList calendars = doc.getElementsByTagName(TAG_CALENDAR);
        for (int i = 0; i < calendars.getLength(); i++) {
            Element calendar = (Element) calendars.item(i);
            NodeList days = calendar.getElementsByTagName(TAG_DAY);
            for (int j = 0; j < days.getLength(); j++) {
                Element day = (Element) days.item(j);
                String key = null;
                String value = null;
                if (day.getElementsByTagName(TAG_DATE).getLength() > 0) {
                    key = day.getElementsByTagName(TAG_DATE).item(0).getTextContent();
                } else if (day.getElementsByTagName(TAG_LABEL).getLength() > 0) {
                    key = day.getElementsByTagName(TAG_LABEL).item(0).getTextContent();
                }
                if (day.getElementsByTagName(TAG_DESCRIPTION).getLength() > 0) {
                    value = day.getElementsByTagName(TAG_DESCRIPTION).item(0).getTextContent();
                    value = STRIPPABLE_MINUTES_PATTERN.matcher(value).replaceAll("");
                }
                super.put(key, value);
            }
        }
    }
}
