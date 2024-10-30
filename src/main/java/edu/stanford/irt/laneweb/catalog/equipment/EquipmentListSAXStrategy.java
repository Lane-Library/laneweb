package edu.stanford.irt.laneweb.catalog.equipment;

import java.util.List;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EquipmentListSAXStrategy extends AbstractXHTMLSAXStrategy<List<Equipment>> {

    @Override
    public void toSAX(final List<Equipment> list, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            for (Equipment equipment : list) {
                String title = equipment.getTitle();
                String count = equipment.getCount();
                startDivWithClass(xmlConsumer, "row");
                startDivWithClass(xmlConsumer, "cell");
                XMLUtils.data(xmlConsumer, title);
                endDiv(xmlConsumer);
                startDivWithClass(xmlConsumer, "cell");
                if ("0".equals(count)) {
                    XMLUtils.data(xmlConsumer, "Checked out");
                } else {
                    XMLUtils.data(xmlConsumer, count);
                    XMLUtils.data(xmlConsumer, " Available ");
                }
                endDiv(xmlConsumer);
                startDivWithClass(xmlConsumer, "cell");
                XMLUtils.data(xmlConsumer, equipment.getNote());
                endDiv(xmlConsumer);
                endDiv(xmlConsumer);
            }
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
