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
        String normedString = title.toLowerCase();
        StringBuilder sb = new StringBuilder("fa-4x ");
        if (normedString.contains("ipad")) {
            sb.append("fa-solid fa-tablet-screen-button");
        } else if (normedString.contains("apple")) {
            sb.append("fa-brands fa-apple");
        } else if (normedString.contains("android")) {
            sb.append("fa-brands fa-android");
        } else if (normedString.contains("keyboard")) {
            sb.append("fa-solid fa-keyboard");
        } else if (normedString.contains("headphones")) {
            sb.append("fa-solid fa-headphones");
        } else if (normedString.contains("tablet")) {
            sb.append("fa-solid fa-tablet-screen-button");
        } else if (normedString.contains("usb")) {
            sb.append("fa-brands fa-usb");
        } else if (normedString.contains("charger")) {
            sb.append("fa-regular fa-battery-bolt");
        } else if (normedString.contains("cable")) {
            sb.append("fa-brands fa-gg");
        } else if (normedString.contains("recorder")) {
            sb.append("fa-solid fa-microphone");
        } else if (normedString.contains("polling")) {
            sb.append("fa-solid fa-users");
        } else if (normedString.contains("magnifying")) {
            sb.append("fa-solid fa-magnifying-glass");
        } else if (normedString.contains("key")) {
            sb.append("fa-regular fa-key");
        }
        return sb.toString();
    }
}
