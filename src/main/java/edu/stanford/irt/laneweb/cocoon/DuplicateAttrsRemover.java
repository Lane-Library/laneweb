package edu.stanford.irt.laneweb.cocoon;

import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.cyberneko.html.filters.DefaultFilter;

/*
 * This class remove the double attributes if the list attributes size if bigger than 8
 * net.sf.saxon.om.LargeAttributeMap check for double attributes and only if the size is superior to 8
 * net.sf.saxon.om.SmallAttributeMap.LIMIT = 8
 */

public class DuplicateAttrsRemover extends DefaultFilter {

    private static final int LIMIT = 8;

    @Override
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) {
        removeDuplicateAttributes(attributes);
        super.startElement(element, attributes, augs);
    }

    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) {
        removeDuplicateAttributes(attributes);
        super.emptyElement(element, attributes, augs);
    }

    private void removeDuplicateAttributes(XMLAttributes attributes) {
        if (attributes.getLength() > LIMIT) {
            List<String> attributeNames = new ArrayList<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                if (attributeNames.contains(name)) {
                    attributes.removeAttributeAt(i);
                }
                attributeNames.add(name);
            }
        }
    }
}
