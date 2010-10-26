package edu.stanford.irt.laneweb.search;

import org.xml.sax.Attributes;

import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

public class SearchContentXMLFilter extends XMLFilterImpl {

    public SearchContentXMLFilter() {}
    
    public SearchContentXMLFilter(XMLReader parent) {
        super(parent);
        
    }

    public void unparsedEntityDecl(String name, String publicId,String systemId, String notationName)
    throws SAXException{
        super.unparsedEntityDecl("DOCTYPE", null, null, null);
        
    }

    
    /**
     * Filter the Namespace URI for start-element events.
     */
    public void startDocument(  ) throws SAXException {
        super.unparsedEntityDecl("DOCTYPE", null, null, null);
        //do nothing
    }

    /**
     * Filter the Namespace URI for end-element events.
     */
    public void endDocument() throws SAXException {
        //do nothing 
    }
}
