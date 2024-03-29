package edu.stanford.irt.laneweb.catalog.equipment;

import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EquipmentListSAXStrategy extends AbstractXHTMLSAXStrategy<List<Equipment>> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    @Override
    public void toSAX(final List<Equipment> list, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            startUl(xmlConsumer);
            for (Equipment equipment : list) {
                String bid = equipment.getBibID();
                String title = equipment.getTitle();
                String count = equipment.getCount();
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "resource");
                atts.addAttribute(XHTML_NS, "data-bibid", "data-bibid", CDATA, bid);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "li", atts);
                startDivWithClass(xmlConsumer, "pure-g");
                startDivWithClass(xmlConsumer, "pure-u-1-6");
                startDivWithClass(xmlConsumer, "equipment-icon");
                AttributesImpl iconAtts = new AttributesImpl();
                iconAtts.addAttribute(XHTML_NS, "aria-hidden", "aria-hidden", CDATA, "true");
                iconAtts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, iconClass(title));
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "i", iconAtts);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "i");
                endDiv(xmlConsumer);
                endDiv(xmlConsumer);
                startDivWithClass(xmlConsumer, "pure-u-5-6");
                startDiv(xmlConsumer);
                XMLUtils.data(xmlConsumer, title);
                endDiv(xmlConsumer);
                startDiv(xmlConsumer);
                XMLUtils.data(xmlConsumer, equipment.getNote());
                endDiv(xmlConsumer);
                startDiv(xmlConsumer);
                createStrong(xmlConsumer, "Status: ");
                if ("0".equals(count)) {
                    XMLUtils.data(xmlConsumer, "Checked out");
                } else {
                    XMLUtils.data(xmlConsumer, "Available ");
                    createStrong(xmlConsumer, count);
                }
                endDiv(xmlConsumer);
                endDiv(xmlConsumer);
                endDiv(xmlConsumer);
                endLi(xmlConsumer);
            }
            endUl(xmlConsumer);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private String iconClass(final String title) {
        StringBuilder sb = new StringBuilder("fa-4x ");
        if (title.contains("iPad")) {
            sb.append("fa-solid fa-tablet-screen-button");
        } else if (title.contains("Apple")) {
            sb.append("fa-brands fa-apple");
        } else if (title.contains("Android")) {
            sb.append("fa-brands fa-android");
        } else if (title.contains("Keyboard")) {
            sb.append("fa-solid fa-keyboard");
        } else if (title.contains("Headphones")) {
            sb.append("fa-solid fa-headphones");
        } else if (title.contains("Tablet")) {
            sb.append("fa-solid fa-tablet-screen-button");
        } else if (title.contains("USB")) {
            sb.append("fa-brands fa-usb");
        } else if (title.contains("Cable")) {
            sb.append("fa-brands fa-gg");
        } else if (title.contains("Recorder")) {
            sb.append("fa-solid fa-microphone");
        } else if (title.contains("Polling")) {
            sb.append("fa-solid fa-users");
        } else if (title.contains("magnifying")) {
            sb.append("fa-solid fa-magnifying-glass");
        }
        return sb.toString();
    }
}
