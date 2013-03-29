package edu.stanford.irt.laneweb.hours;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StreamDaysMapping extends HashMap<String, String> {

    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

    private static final Logger LOG = LoggerFactory.getLogger(StreamDaysMapping.class);

    private static final long serialVersionUID = 1L;

    private static final String STRIPPABLE_MINUTES = ":00";

    private static final String TAG_CALENDAR = "calendar";

    private static final String TAG_DATE = "date";

    private static final String TAG_DAY = "day";

    private static final String TAG_DESCRIPTION = "description";

    private static final String TAG_LABEL = "label";

    public StreamDaysMapping(final InputStream inputStream) {
        try {
            DocumentBuilder builder = FACTORY.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
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
                        value = value.replaceAll(STRIPPABLE_MINUTES, "");
                    }
                    super.put(key, value);
                }
            }
        } catch (ParserConfigurationException e) {
            LOG.error("error parsing hours file", e);
        } catch (SAXException e) {
            LOG.error("error parsing hours file", e);
        } catch (IOException e) {
            LOG.error("IO error while parsing hours file", e);
        }
    }
}
