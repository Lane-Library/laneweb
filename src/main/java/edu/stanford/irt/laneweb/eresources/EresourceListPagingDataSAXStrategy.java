package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceListPagingDataSAXStrategy implements SAXStrategy<EresourceListPagingData> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";
    
    private static final String DIV = "div";

    private static final String EMPTY_NS = "";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	private static final String A = "a";

	private static final String HREF = "href";

	private static final String UL = "ul";

	private static final String LI = "li";

	private static final String SPAN = "span";

    @Override
	public void toSAX(final EresourceListPagingData pagingData, final XMLConsumer xmlConsumer) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "resourceListPagination");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "yui-g");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "yui-u first");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            int size = pagingData.getSize();
            int length = pagingData.getLength();
            int start = pagingData.getStart();
            StringBuilder sb = new StringBuilder("Displaying ");
            String hrefBase = pagingData.getBaseQuery();
            if (size > length) {
                sb.append(start + 1).append(" to ").append(start + length).append(" of ");
                XMLUtils.data(xmlConsumer, sb.toString());
                atts = new AttributesImpl();
                sb.setLength(0);
                sb.append("?");
                if (hrefBase.length() > 0) {
                    sb.append(hrefBase).append("&");
                }
                sb.append("page=all");
                atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                sb.setLength(0);
                sb.append(Integer.toString(size)).append(" matches");
                XMLUtils.data(xmlConsumer, sb.toString());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            } else {
                sb.append("all ").append(size).append(" matches");
                XMLUtils.data(xmlConsumer, sb.toString());
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "plsContainer");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
            XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, "#");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            String alpha = pagingData.getAlpha();
            sb.setLength(0);
            sb.append("Choose ");
            if (alpha == null || alpha.length() == 0 || "all".equals(alpha)) {
            	sb.append("A-Z");
            } else {
            	alpha = alpha.toUpperCase();
            	sb.append(alpha).append("a-").append(alpha).append('z');
            }
            XMLUtils.data(xmlConsumer, sb.toString());
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "pagingLabels");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, LI, atts);
            XMLUtils.startElement(xmlConsumer, XHTML_NS, UL);
            int i = 1;
            AttributesImpl plDash = new AttributesImpl();
            plDash.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "plDash");
            AttributesImpl plResults = new AttributesImpl();
            plResults.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "plResults");
            for (PagingLabel label : pagingData.getPagingLabels()) {
            	

                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
                sb.setLength(0);
                sb.append("?");
                if (hrefBase.length() > 0) {
                    sb.append(hrefBase).append("&");
                }
                sb.append("page=").append(i);
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                XMLUtils.data(xmlConsumer, label.getStart());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
                XMLUtils.startElement(xmlConsumer, SPAN, SPAN, plDash);
                XMLUtils.data(xmlConsumer, " — ");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                XMLUtils.data(xmlConsumer, label.getEnd());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI, plResults);
                XMLUtils.data(xmlConsumer, " (" + label.getResults() + ")");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
                i++;
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "seeAll");
            sb.setLength(0);
            sb.append("?");
            if (hrefBase.length() > 0) {
                sb.append(hrefBase).append("&");
            }
            sb.append("page=all");
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            XMLUtils.data(xmlConsumer, "See All");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
