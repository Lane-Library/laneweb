package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PagingDataSAXStrategy implements SAXStrategy<PagingData> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";
    
    private static final String DIV = "div";

    private static final String EMPTY_NS = "";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	private static final String A = "a";

	private static final String STYLE = "style";

	private static final String ID = "id";

	private static final String HREF = "href";

    public void toSAX(final PagingData pagingData, final XMLConsumer xmlConsumer) {
        try {
            int size = pagingData.getSize();
            int length = pagingData.getLength();
            int start = pagingData.getStart();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "results-nav");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "yui-g");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "yui-u first");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
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
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "yui-u");
            atts.addAttribute(EMPTY_NS, STYLE, STYLE, CDATA, "text-align:right");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            if (size > length) {
                // <a id="seeAll" href="?{$no-page-query-string}page=all">See All</a>
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, ID, ID, CDATA, "seeAll");
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
                int pages = pagingData.getPages();
                int page = pagingData.getPage();
                for (int i = 0; i < pages; i++) {
                    if (i == page) {
                        XMLUtils.data(xmlConsumer, Integer.toString(i + 1));
                    } else {
                        // <a href="?{$query-string}page={number($page) + 1}"><xsl:value-of
                        // select="number($page) + 1"/></a>
                        atts = new AttributesImpl();
                        sb.setLength(0);
                        sb.append("?");
                        if (hrefBase.length() > 0) {
                            sb.append(hrefBase).append("&");
                        }
                        sb.append("page=").append(i + 1);
                        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
                        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                        XMLUtils.data(xmlConsumer, Integer.toString(i + 1));
                        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                    }
                    if (i + 1 < pages) {
                        XMLUtils.data(xmlConsumer, " | ");
                    }
                }
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
