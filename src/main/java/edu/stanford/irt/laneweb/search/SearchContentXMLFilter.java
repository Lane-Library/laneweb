package edu.stanford.irt.laneweb.search;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

public class SearchContentXMLFilter extends XMLFilterImpl {

    
    public SearchContentXMLFilter(XMLReader parent) {
        super(parent);
    }

   
    
    /**
     * Filter the Namespace URI for start-element events.
     */
    public void startDocument(  ) throws SAXException {
        //do nothing
    }

    /**
     * Filter the Namespace URI for end-element events.
     */
    public void endDocument() throws SAXException {
        //do nothing 
    }
}
