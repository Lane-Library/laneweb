package edu.stanford.irt.laneweb.cocoon;

import org.xml.sax.ext.DefaultHandler2;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class NOOPXMLConsumer extends DefaultHandler2 implements XMLConsumer {
    
    public static final XMLConsumer INSTANCE = new NOOPXMLConsumer();
}
