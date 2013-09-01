package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceListPagingDataSAXStrategy extends AbstractXHTMLSAXStrategy<EresourceListPagingData> {

    private static final Pattern ALPHA_PATTERN = Pattern.compile("(^|.+&)a=([a-z])(&.+|$)");

    private static final int LINK_LENGTH = 45;

    private static final int TEXT_LENGTH = 37;

    @Override
    public void toSAX(final EresourceListPagingData pagingData, final XMLConsumer xmlConsumer) {
        try {
            startDivWithClass(xmlConsumer, "resourceListPagination");
            createDisplayingMarkup(xmlConsumer, pagingData);
            if (pagingData.getSize() > pagingData.getLength()) {
                String hrefBase = pagingData.getBaseQuery();
                createPagingButton(xmlConsumer, hrefBase, pagingData);
                createSeeAllMarkup(xmlConsumer, hrefBase);
            }
            endDiv(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void createDisplayingMarkup(final XMLConsumer xmlConsumer, final EresourceListPagingData pagingData)
            throws SAXException {
        String hrefBase = pagingData.getBaseQuery();
        int size = pagingData.getSize();
        int length = pagingData.getLength();
        int start = pagingData.getStart();
        if (size > length) {
            XMLUtils.data(xmlConsumer, "Displaying ");
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
            createAnchor(xmlConsumer, sb.toString(), size + " matches");
        } else {
            StringBuilder sb = new StringBuilder("Displaying ");
            sb.append("all ").append(size).append(" matches");
            XMLUtils.data(xmlConsumer, sb.toString());
        }
    }

    private void createPagingButton(final XMLConsumer xmlConsumer, final String hrefBase, EresourceListPagingData pagingData) throws SAXException {
        startDivWithClass(xmlConsumer, "pagingButton gray-btn");
        String alpha = null;
        Matcher matcher = ALPHA_PATTERN.matcher(hrefBase);
        if (matcher.matches()) {
            alpha = matcher.group(2);
        }
        StringBuilder sb = new StringBuilder("Choose ");
        if (alpha == null) {
            sb.append("A-Z");
        } else {
            alpha = alpha.toUpperCase();
            sb.append(alpha).append("a-").append(alpha).append('z');
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

    private void createSeeAllMarkup(final XMLConsumer xmlConsumer, final String hrefBase) throws SAXException {
        StringBuilder sb = new StringBuilder("?");
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append("&");
        }
        sb.append("page=all");
        createAnchorWithClass(xmlConsumer, sb.toString(), "seeAll", "See All");
    }
}
