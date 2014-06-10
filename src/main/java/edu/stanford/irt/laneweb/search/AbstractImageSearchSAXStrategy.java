package edu.stanford.irt.laneweb.search;



import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public abstract class AbstractImageSearchSAXStrategy<T extends Object> extends AbstractXHTMLSAXStrategy<T>{

	private static final String CDATA = "CDATA";
	private static final String DIV = "div";
	private static final String ANCHOR = "a";
	private static final String CLASS = "class";
	private static final String H4 = "h4";
	private static final String REL = "rel";
	private static final String ID = "id";
	private static final String SRC = "src";
	private static final String IMAGE = "image";
	
    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    protected static final String UL = "ul";

    
	protected void generateImages(XMLConsumer xmlConsumer, String id, String title, String url, String thumbnailSrc, String imageSrc) throws SAXException {
		 AttributesImpl atts = new AttributesImpl();
		 if(null != id && imageSrc != null && !"".equals(imageSrc)){
			atts.addAttribute(XHTML_NS, ID, ID, CDATA, id);
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "yui3-tooltip-trigger");
		 }
	     XMLUtils.startElement(xmlConsumer, XHTML_NS, "li" , atts);
	     startAnchor(xmlConsumer, url);
		startElementWithId(xmlConsumer, DIV, "image");
		createImage(xmlConsumer, thumbnailSrc);
		endDiv(xmlConsumer);
		startDiv(xmlConsumer);
		XMLUtils.data(xmlConsumer, title);
		endDiv(xmlConsumer);
		endAnchor(xmlConsumer);
		endLi(xmlConsumer);
		
	}

	
	protected void createTitle(XMLConsumer xmlConsumer, String id, String title, String hits, String total, String url) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, ID, ID, CDATA, "searchImageTitle");
		atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "plain");
		XMLUtils.startElement(xmlConsumer, XHTML_NS, H4, atts);
		XMLUtils.data(xmlConsumer, title);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, H4);
		startElementWithId(xmlConsumer, DIV, "copyright");
		XMLUtils.data(xmlConsumer, " ".concat(hits).concat(" of "));
		startAnchor(xmlConsumer,url);
		XMLUtils.data(xmlConsumer, total.concat(" images found"));
		endAnchor(xmlConsumer);
		atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, REL, REL, CDATA, "popup local ".concat(id));
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.data(xmlConsumer, "Copyright Information");
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		endDiv(xmlConsumer);
	}
	
	
	protected void startElementWithId(final XMLConsumer xmlConsumer, final String name, final String id) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, id == null ? "" : id);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, name, atts);
    }
	
	protected void createImage(XMLConsumer xmlConsumer, String src)throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, src);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
	}
	
	protected void generateTooltipsImage(XMLConsumer xmlConsumer, String id, String imageSrc) throws SAXException {
		if (null != imageSrc && !"".equals(imageSrc)) {
			startElementWithId(xmlConsumer, "span",id.concat("Tooltip"));
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, imageSrc);
			atts.addAttribute(XHTML_NS, "style", "style", CDATA,"max-width: 300px;max-height: 240px");
			XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
			XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
			XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
		}
	}
	

}
