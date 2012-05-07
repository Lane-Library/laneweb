package edu.stanford.irt.laneweb.util;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

public interface XMLizable {

    void toSAX(XMLConsumer xmlConsumer) throws SAXException;
}
