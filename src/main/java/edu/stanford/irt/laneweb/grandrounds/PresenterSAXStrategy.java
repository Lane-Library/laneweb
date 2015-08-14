package edu.stanford.irt.laneweb.grandrounds;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Affiliation;
import edu.stanford.irt.grandrounds.Presenter;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PresenterSAXStrategy implements SAXStrategy<Presenter> {

    @Override
    public void toSAX(final Presenter presenter, final XMLConsumer xmlConsumer) {
        try {
            AttributesImpl atts = new AttributesImpl();
            if (presenter.getId() != -1) {
                atts.addAttribute("", "id", "id", "CDATA", Integer.toString(presenter.getId()));
            }
            XMLUtils.startElement(xmlConsumer, "", "presenter", atts);
            XMLUtils.maybeCreateElement(xmlConsumer, "", "name", presenter.getName());
            XMLUtils.maybeCreateElement(xmlConsumer, "", "uri", presenter.getURI());
            for (Affiliation affiliation : presenter.getAffiliations()) {
                XMLUtils.startElement(xmlConsumer, "", "affiliation");
                XMLUtils.maybeCreateElement(xmlConsumer, "", "title", affiliation.getTitle());
                XMLUtils.maybeCreateElement(xmlConsumer, "", "name", affiliation.getName());
                XMLUtils.createElementNS(xmlConsumer, "", "start", affiliation.getStart());
                XMLUtils.createElementNS(xmlConsumer, "", "end", affiliation.getEnd());
                XMLUtils.endElement(xmlConsumer, "", "affiliation");
            }
            XMLUtils.endElement(xmlConsumer, "", "presenter");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
