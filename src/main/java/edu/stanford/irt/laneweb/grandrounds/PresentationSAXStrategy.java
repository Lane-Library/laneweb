package edu.stanford.irt.laneweb.grandrounds;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.format.DateTimeFormatter;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.grandrounds.Presenter;
import edu.stanford.irt.grandrounds.Video;
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
            for (Presenter presenter : presentation.getPresenters()) {
                int id = presenter.getId();
                if (id == -1) {
                    XMLUtils.startElement(xmlConsumer, "", "presenter");
                    String name = presenter.getName();
                    if (!Normalizer.isNormalized(name, Form.NFKC)) {
                        name = Normalizer.normalize(name, Form.NFKC);
                    }
                    XMLUtils.createElementNS(xmlConsumer, "", "name", name);
                    XMLUtils.endElement(xmlConsumer, "", "presenter");
                } else {
                    atts = new AttributesImpl();
                    atts.addAttribute("", "idref", "idref", "CDATA", Integer.toString(id));
                    XMLUtils.startElement(xmlConsumer, "", "presenter", atts);
                    XMLUtils.endElement(xmlConsumer, "", "presenter");
                }
            }
            for (String description : presentation.getDescriptions()) {
                XMLUtils.createElementNS(xmlConsumer, "", "description", description);
            }
            for (Video video : presentation.getVideos()) {
                XMLUtils.startElement(xmlConsumer, "", "video");
                XMLUtils.maybeCreateElement(xmlConsumer, "", "uri", video.getURI());
                XMLUtils.endElement(xmlConsumer, "", "video");
            }
            XMLUtils.endElement(xmlConsumer, "", "presentation");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
