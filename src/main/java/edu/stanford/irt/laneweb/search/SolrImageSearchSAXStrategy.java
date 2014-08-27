package edu.stanford.irt.laneweb.search;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategy extends AbstractXHTMLSAXStrategy<Map<String, Object>> {

	private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	private static final String CDATA = "CDATA";

	private static final String CLASS = "class";

	private static final String DIV = "div";

	private static final String UL = "ul";

	private static final String ANCHOR = "a";

	private static final String HREF = "href";

	private static final String ID = "id";

	private static final String IMAGE = "image";

	private static final String REL = "rel";

	private static final String SRC = "src";

	private static final String STYLE = "style";

	public void toSAX(final Map<String, Object> result, final XMLConsumer xmlConsumer) {
		try {
			xmlConsumer.startDocument();
			startDiv(xmlConsumer);
			toSAXResult(result, xmlConsumer);
			endDiv(xmlConsumer);
			xmlConsumer.endDocument();
		} catch (SAXException e) {
			throw new LanewebException(e);
		}
	}

	private void toSAXResult(final Map<String, Object> result, final XMLConsumer xmlConsumer) throws SAXException {
		@SuppressWarnings("unchecked")
		Page<Image> page = ((Page<Image>) result.get("page"));
		List<Image> images = page.getContent();
		generateSumaryResult(xmlConsumer, page, result, true);
		startElementWithId(xmlConsumer, UL, "imageList");
		for (Image image : images) {
			generateImages(xmlConsumer, image.getId(), image.getTitle(), image.getPageUrl(), image.getThumbnailSrc(),
			        image.getSrc(), String.valueOf(image.getCopyrightValue()));
		}
		endUl(xmlConsumer);
		generateSumaryResult(xmlConsumer, page, result, false);
		generateTooltips(xmlConsumer, images);
	}

	protected void generateTooltips(final XMLConsumer xmlConsumer, final List<Image> images) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "display:none");
		atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "tooltips");
		XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
		for (Image image : images) {
			generateTooltipsImage(xmlConsumer, image.getId(), image.getSrc());
		}
		endDiv(xmlConsumer);
	}

	protected void createImage(final XMLConsumer xmlConsumer, final String id, final String thumbnailSrc,
	        final String src) throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, thumbnailSrc);
		if (null != id && src != null && !"".equals(src)) {
			atts.addAttribute(XHTML_NS, ID, ID, CDATA, id);
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "yui3-tooltip-trigger");
		}
		XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
	}

	protected void generatePagination(final XMLConsumer xmlConsumer, final Page<Image> page, final String path)
	        throws SAXException {
		startDivWithClass(xmlConsumer, "pagination");
		AttributesImpl atts = new AttributesImpl();
		if (page.getNumber() != 0) {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-fast-backward  actived");
			atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, path + "0");
		} else {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-fast-backward disabled");
		}
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		atts = new AttributesImpl();
		if (page.getNumber() != 0) {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-step-backward  actived");
			atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, path + (page.getNumber() - 1));
		} else {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-step-backward disabled");
		}
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		String pageText = "Page " + (page.getNumber() + 1) + " of " + page.getTotalPages();
		XMLUtils.data(xmlConsumer, pageText);
		atts = new AttributesImpl();
		if (page.getNumber() != page.getTotalPages() - 1) {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-step-forward actived");
			atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, path + (page.getNumber() + 1));
		} else {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-step-forward disabled");
		}
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		atts = new AttributesImpl();
		if (page.getNumber() != page.getTotalPages() - 1) {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-fast-forward  actived");
			atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, path + (page.getTotalPages() - 1));
		} else {
			atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-fast-forward disabled");
		}
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		endDiv(xmlConsumer);
	}

	protected void generateResult(final XMLConsumer xmlConsumer, final Page<Image> page,
	        final Map<String, Object> result, boolean isTopScreen) throws SAXException {
		String numberResult = String.valueOf(page.getSize() * page.getNumber() + 1);
		String number = String.valueOf(page.getSize() * page.getNumber() + page.getNumberOfElements());
		startDivWithClass(xmlConsumer, "result");
		if (page.hasContent()) {
			XMLUtils.data(xmlConsumer, "Results " + numberResult + " to " + number + " of " + page.getTotalElements());
		} else if (isTopScreen) {
			XMLUtils.data(xmlConsumer,
			        "No " + result.get("tab") + " images are available with search term, " + result.get("searchTerm"));
		}
		endDiv(xmlConsumer);
	}

	private void generateSumaryResult(final XMLConsumer xmlConsumer, final Page<Image> page,
	        final Map<String, Object> result, boolean isTopScreen) throws SAXException {
		startDivWithClass(xmlConsumer, "result-summary");
		generateResult(xmlConsumer, page, result, isTopScreen);
		if (page.getTotalPages() > 1) {
			generatePagination(xmlConsumer, page, (String) result.get("path"));
		}
		endDiv(xmlConsumer);

	}

	protected void generateImages(final XMLConsumer xmlConsumer, final String id, String title, final String url,
	        final String thumbnailSrc, final String imageSrc, final String copyright) throws SAXException {

		XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
		startAnchor(xmlConsumer, url);
		startElementWithId(xmlConsumer, DIV, "image");
		createImage(xmlConsumer, id, thumbnailSrc, imageSrc);
		endDiv(xmlConsumer);
		startDiv(xmlConsumer);
		if (title.length() > 90) {
			title = title.substring(0, 90).concat("....");
		}
		XMLUtils.data(xmlConsumer, title);
		endDiv(xmlConsumer);
		endAnchor(xmlConsumer);
		generateWebsiteSource(xmlConsumer, id, url);
		generateCopyright(xmlConsumer, id);

		endLi(xmlConsumer);
	}

	private void generateCopyright(final XMLConsumer xmlConsumer, final String id) throws SAXException {
		startElementWithId(xmlConsumer, DIV, "copyright");
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, REL, REL, CDATA,
		        "popup local ".concat(id.substring(0, id.indexOf("/")).replaceAll("\\.", "-")));
		XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
		XMLUtils.data(xmlConsumer, "Copyright Information ");
		atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-arrow-right");
		XMLUtils.startElement(xmlConsumer, XHTML_NS, "i", atts);
		XMLUtils.endElement(xmlConsumer, XHTML_NS, "i");
		XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
		endDiv(xmlConsumer);
	}

	private void generateWebsiteSource(final XMLConsumer xmlConsumer, final String id, final String url)
	        throws SAXException {
		startElementWithId(xmlConsumer, DIV, "website-id");
		XMLUtils.data(xmlConsumer, "Source: ");
		startAnchor(xmlConsumer, url);
		if (id.startsWith("lane.bassett")) {
			XMLUtils.data(xmlConsumer, "Bassett");
		} else if (id.startsWith("ncbi.nlm.nih.gov")) {
			XMLUtils.data(xmlConsumer, "PubMed Central");
		} else {
			XMLUtils.data(xmlConsumer, id.substring(0, id.indexOf("/")));
		}
		endAnchor(xmlConsumer);
		endDiv(xmlConsumer);
	}

	protected void generateTooltipsImage(final XMLConsumer xmlConsumer, final String id, final String imageSrc)
	        throws SAXException {
		if (null != id && null != imageSrc && !"".equals(imageSrc)) {
			startElementWithId(xmlConsumer, "span", id.concat("Tooltip"));
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, imageSrc);
			atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "max-width: 300px;max-height: 240px");
			XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
			XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
			XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
		}
	}

	protected void startElementWithId(final XMLConsumer xmlConsumer, final String name, final String id)
	        throws SAXException {
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(XHTML_NS, ID, ID, CDATA, id == null ? "" : id);
		XMLUtils.startElement(xmlConsumer, XHTML_NS, name, atts);
	}

}
