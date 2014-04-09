package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ImageSearchSAXStrategy implements
		SAXStrategy<HashMap<String, Object>> {

	private static final String CDATA = "CDATA";
	private static final String DIV = "div";
	private static final String HEIGHT = "height";
	private static final String SPAN = "span";
	private static final String ANCHOR = "a";
	private static final String HREF = "href";
	private static final String UL = "ul";
	private static final String LI = "li";
	private static final String ID = "id";
	private static final String SRC = "src";
	private static final String IMAGE = "image";
    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	private final int MAX_BASSETT_RESULT = 12;
	private static final String BASSETT_PAGE_PATH = "/biomed-resources/bassett/bassettView.html?bn=";
	private static final String BASSETT_ICON_SRC_URI = "http://elane.stanford.edu/public/L254573/small/";
	private static final String BASSETT_SEARCH_URL = "http://lane.stanford.edu/search.html?source=bassett&q=";

	
	@SuppressWarnings("unchecked")
    @Override
	public void toSAX(HashMap<String, Object> result, XMLConsumer xmlConsumer) {

		try {
			xmlConsumer.startDocument();
			XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV);
			
			//Metasearch content
			Result metaSearchResult = (Result) result.get(ImageSearchGenerator.METASEARCH_RESULT);
			Collection<Result> engines = metaSearchResult.getChildren();
			String hits = null;
			for (Result engineResult : engines) {
				XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV);
				Collection<Result> resources = engineResult.getChildren();
				hits = engineResult.getHits();
				String url = null;
				for (Result resource : resources) {
					if(resource.getId().indexOf("_content") == -1){
						url = resource.getURL();
					}
					if (resource.getHits() != null && !"0".equals(resource.getHits()) && resource.getId().endsWith("_content")) {
						createTitle(xmlConsumer,resource.getId(), engineResult.getDescription(), resource.getHits(), hits, url);
					}
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute(XHTML_NS, ID, ID, CDATA, "imageList");
					XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
					Collection<Result> contents = resource.getChildren();
					for (Result content : contents) {
						if (content.getId().contains("_content_")) {
							ContentResult contentResult = (ContentResult) content;
							generateImages(xmlConsumer, contentResult.getTitle(), contentResult.getURL(), contentResult.getDescription());
						}
					}
					XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
				}
				XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
			}
			
			
			//Bassett content
			String query = metaSearchResult.getQuery().toString();
			List<BassettImage> bassettResult = (List<BassettImage>)result.get(ImageSearchGenerator.BASSETT_RESULT);
			 int bassettMaxResult = ((bassettResult.size() > MAX_BASSETT_RESULT) ? MAX_BASSETT_RESULT: bassettResult.size());
			 if (bassettResult.size() != 0) {
				 createTitle(xmlConsumer, "bassett", "Bassett",String.valueOf(bassettMaxResult),String.valueOf(bassettResult.size()),
				 BASSETT_SEARCH_URL.concat(query));
				 AttributesImpl atts = new AttributesImpl();
				 atts.addAttribute(XHTML_NS, ID, ID, CDATA, "imageList");
				 XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
				 for (BassettImage bassettImage : bassettResult) {
					 String titleAndSubtitle = bassettImage.getTitle();
					 String bassettTitle = "";
	                 if (titleAndSubtitle != null) {
	                         String[] title = titleAndSubtitle.split("\\.");
	                         bassettTitle = title[0];
	                 }
				 generateImages(xmlConsumer, bassettTitle, BASSETT_PAGE_PATH.concat(bassettImage.getBassettNumber()), BASSETT_ICON_SRC_URI.concat(bassettImage.getImage()));
				 if (--bassettMaxResult == 0)
				 break;
				 }
				 XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
			 }
			
			XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
			xmlConsumer.endDocument();
		} catch (SAXException e) {
			throw new LanewebException(e);
		}

	}

	private void generateImages(XMLConsumer xmlConsumer, String title, String url, String src) throws SAXException {
		XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, url);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, src);
		atts.addAttribute(XHTML_NS, HEIGHT, HEIGHT, CDATA, "120px");
		XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV);
		XMLUtils.data(xmlConsumer, title);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
	}

	private void createTitle(XMLConsumer xmlConsumer, String id, String title, String hits, String total, String url) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, ID, ID, CDATA, "searchImageTitle");
		XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN);
		XMLUtils.data(xmlConsumer, title.concat(" "));
		XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
		XMLUtils.data(xmlConsumer, hits.concat(" of "));
		atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, url);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.data(xmlConsumer, total.concat(" images found"));
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, ID, ID, CDATA, id);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
		XMLUtils.data(xmlConsumer, "Copyright Information");
		XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
	}
	
	

}
