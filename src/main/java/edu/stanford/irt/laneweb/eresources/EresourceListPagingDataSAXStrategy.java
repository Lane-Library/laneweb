package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceListPagingDataSAXStrategy extends AbstractXHTMLSAXStrategy<EresourceListPagingData> {

    private static final int LINK_LENGTH = 40;

    private static final int TEXT_LENGTH = 37;

    @Override
    public void toSAX(final EresourceListPagingData pagingData, final XMLConsumer xmlConsumer) {
        try {
            startDivWithClass(xmlConsumer, "resourceListPagination");
            String alpha = pagingData.getAlpha();
            if (alpha == null || "all".equals(alpha)) {
                alpha = "";
            } else {
                alpha = alpha.toUpperCase();
            }
            createDisplayingMarkup(xmlConsumer, pagingData, alpha);
            if (pagingData.getSize() > pagingData.getLength()) {
                String hrefBase = pagingData.getBaseQuery();
                createPagingButton(xmlConsumer, hrefBase, pagingData, alpha);
                createSeeAllMarkup(xmlConsumer, hrefBase, alpha);
            }
            endDiv(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void createDisplayingMarkup(final XMLConsumer xmlConsumer, final EresourceListPagingData pagingData,
            final String alpha) throws SAXException {
        String hrefBase = pagingData.getBaseQuery();
        int size = pagingData.getSize();
        int length = pagingData.getLength();
        int start = pagingData.getStart();
        String quotedAlpha = maybeGetQuotedAlpha(alpha);
        if (size > length) {
            createSpanWithId(xmlConsumer, "pageStart", Integer.toString(start + 1));
            StringBuilder sb = new StringBuilder(" to ");
            sb.append(start + length).append(" of ");
            XMLUtils.data(xmlConsumer, sb.toString());
            sb.setLength(0);
            sb.append("?");
            if (hrefBase.length() > 0) {
                sb.append(hrefBase).append("&");
            }
            sb.append("page=all");
            createAnchor(xmlConsumer, sb.toString(), size + quotedAlpha + " titles");
        } else {
            StringBuilder sb = new StringBuilder("all ").append(size).append(quotedAlpha).append(" titles");
            XMLUtils.data(xmlConsumer, sb.toString());
        }
    }

    private void createPagingButton(final XMLConsumer xmlConsumer, final String hrefBase, EresourceListPagingData pagingData, final String alpha) throws SAXException {
        startDivWithClass(xmlConsumer, "pagingButton gray-btn");
        StringBuilder sb = new StringBuilder("Choose ");
        if (alpha.isEmpty()) {
            sb.append("A-Z");
        } else {
            String upperAlpha = alpha.toUpperCase();
            sb.append(upperAlpha).append("a-").append(upperAlpha).append('z');
        }
        createSpan(xmlConsumer, sb.toString());
        startUlWithClass(xmlConsumer, "pagingLabels");
        int i = 1;
        for (PagingLabel label : pagingData.getPagingLabels()) {
            createPagingLabelMarkup(xmlConsumer, label, hrefBase, i);
            i++;
        }
        endUl(xmlConsumer);
        endDiv(xmlConsumer);
    }

    private void createPagingLabelMarkup(final XMLConsumer xmlConsumer, final PagingLabel label, final String hrefBase,
            final int i) throws SAXException {
        startLi(xmlConsumer);
        StringBuilder sb = new StringBuilder("?");
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append("&");
        }
        sb.append("page=").append(i);
        startAnchor(xmlConsumer, sb.toString());
        sb.setLength(0);
        sb.append(label.getStart());
        if (sb.length() > TEXT_LENGTH) {
            sb.setLength(TEXT_LENGTH);
        } else {
            sb.append(' ');
        }
        while (sb.length() < LINK_LENGTH) {
            sb.append('.');
        }
        createSpan(xmlConsumer, sb.toString());
        createSpanWithClass(xmlConsumer, "plDash", " — ");
        sb.setLength(0);
        sb.append(label.getEnd());
        if (sb.length() > TEXT_LENGTH) {
            sb.setLength(TEXT_LENGTH);
        } else {
            sb.append(' ');
        }
        while (sb.length() < LINK_LENGTH) {
            sb.append('.');
        }
        sb.append(" ");
        createSpan(xmlConsumer, sb.toString());
        createSpanWithClass(xmlConsumer, "plResults", " (" + label.getResults() + ")");
        endAnchor(xmlConsumer);
        endLi(xmlConsumer);
    }

    private void createSeeAllMarkup(final XMLConsumer xmlConsumer, final String hrefBase, final String alpha)
            throws SAXException {
        StringBuilder sb = new StringBuilder("?");
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append("&");
        }
        sb.append("page=all");
        createAnchorWithClass(xmlConsumer, sb.toString(), "seeAll",
                new StringBuilder("All").append(maybeGetQuotedAlpha(alpha)).append(" titles").toString());
    }

    private String maybeGetQuotedAlpha(final String alpha) {
        String quotedAlpha = null;
        if (alpha.isEmpty()) {
            quotedAlpha = alpha;
        } else {
            quotedAlpha = new StringBuilder(" \"").append(alpha).append('"').toString();
        }
        return quotedAlpha;
    }
}
