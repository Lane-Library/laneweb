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
