package edu.stanford.irt.laneweb.grandrounds;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.GrandRoundsException;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PresentationSAXStrategy implements SAXStrategy<Presentation> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM d yyyy");

    private static final Logger LOG = LoggerFactory.getLogger(PresentationSAXStrategy.class);

    @Override
    public void toSAX(final Presentation presentation, final XMLConsumer xmlConsumer) {
        LocalDate date = null;
        List<URI> uris = null;
        int recordId = presentation.getId();
        try {
            date = presentation.getDate();
            uris = presentation.getURIs();
        } catch (GrandRoundsException e) {
            LOG.error(recordId + " not valid", e);
            return;
        }
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "id", "id", "CDATA", Integer.toString(recordId));
            XMLUtils.startElement(xmlConsumer, "", "presentation", atts);
            XMLUtils.createElementNS(xmlConsumer, "", "date", FORMATTER.format(date));
            XMLUtils.createElementNS(xmlConsumer, "", "title", presentation.getTitle());
            XMLUtils.createElementNS(xmlConsumer, "", "sunet", Boolean.toString(presentation.getSunetRequired()));
            for (String presenterText : presentation.getPresenterList()) {
                XMLUtils.createElementNS(xmlConsumer, "", "presenter", presenterText);
            }
            for (String description : presentation.getDescriptions()) {
                XMLUtils.createElementNS(xmlConsumer, "", "description", description);
            }
            for (URI uri : uris) {
                XMLUtils.maybeCreateElement(xmlConsumer, "", "uri", uri);
            }
            XMLUtils.endElement(xmlConsumer, "", "presentation");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
