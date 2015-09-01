package edu.stanford.irt.laneweb.grandrounds;

import java.net.URI;
import java.time.format.DateTimeFormatter;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PresentationSAXStrategy implements SAXStrategy<Presentation> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM d yyyy");

    @Override
    public void toSAX(final Presentation presentation, final XMLConsumer xmlConsumer) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "id", "id", "CDATA", Integer.toString(presentation.getId()));
            XMLUtils.startElement(xmlConsumer, "", "presentation", atts);
            XMLUtils.createElementNS(xmlConsumer, "", "date", FORMATTER.format(presentation.getDate()));
            XMLUtils.createElementNS(xmlConsumer, "", "title", presentation.getTitle());
            XMLUtils.createElementNS(xmlConsumer, "", "sunet", Boolean.toString(presentation.getSunetRequired()));
            for (String presenterText : presentation.getPresenterList()) {
                XMLUtils.createElementNS(xmlConsumer, "", "presenter", presenterText);
            }
            for (String description : presentation.getDescriptions()) {
                XMLUtils.createElementNS(xmlConsumer, "", "description", description);
            }
            for (URI uri : presentation.getURIs()) {
                XMLUtils.maybeCreateElement(xmlConsumer, "", "uri", uri);
            }
            XMLUtils.endElement(xmlConsumer, "", "presentation");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
