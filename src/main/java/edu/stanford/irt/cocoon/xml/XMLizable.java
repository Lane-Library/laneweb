package edu.stanford.irt.cocoon.xml;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

public interface XMLizable {

    void toSAX(XMLConsumer xmlConsumer) throws SAXException;
}
