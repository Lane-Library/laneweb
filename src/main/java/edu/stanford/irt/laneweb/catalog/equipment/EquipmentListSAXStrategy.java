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
    
    private static final String SOLID_SVG_PATH = "/resources/svg/solid.svg";
    
    private static final String BRANDS_SVG_PATH = "/resources/svg/brands.svg";
    
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
                this.createIconElement(xmlConsumer, title);
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

    private void createIconElement( final XMLConsumer xmlConsumer,final String title) throws SAXException {
        if (title.contains("iPad")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "tablet-screen-button");
        } else if (title.contains("Apple")) {
            createSvg(xmlConsumer, "fa-4x", BRANDS_SVG_PATH, "apple");
        } else if (title.contains("Android")) {
            createSvg(xmlConsumer, "fa-4x", BRANDS_SVG_PATH, "android");            
        } else if (title.contains("Keyboard")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "keyboard");
        } else if (title.contains("Headphones")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "headphones");
        } else if (title.contains("Tablet")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "tablet-screen-button");
        } else if (title.contains("USB")) {
            createSvg(xmlConsumer, "fa-4x", BRANDS_SVG_PATH, "usb");
        } else if (title.contains("Cable")) {
            createSvg(xmlConsumer, "fa-4x", BRANDS_SVG_PATH, "gg");
        } else if (title.contains("Recorder")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "microphone");
        } else if (title.contains("Polling")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "users");
        } else if (title.contains("magnifying")) {
            createSvg(xmlConsumer, "fa-4x", SOLID_SVG_PATH, "magnifying-glass");
        }
    }
}
