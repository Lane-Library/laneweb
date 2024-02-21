package edu.stanford.irt.laneweb.images;

import java.util.List;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.bassett.model.BassettImage;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class BassettImageListSAXStrategy implements SAXStrategy<Page<BassettImage>> {

    private static final String BASSETT = "bassett";

    private static final String BASSETT_IMAGE = "bassett_image";

    private static final String BASSETT_NUMBER = "bassett_number";

    private static final String BASSETTS = "bassetts";

    private static final String CURRENT_PAGE = "current-page";

    private static final String DESCRIPTION = "description";

    private static final String DIAGRAM = "diagram_image";

    private static final String IMAGE_NUMBER_LOW = "image-number-low";

    private static final String IMAGE_NUMBER_UP = "image-number-up";

    private static final String LEGEND = "legend";

    private static final String LEGEND_IMAGE = "legend_image";

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String NEXT_PAGE = "next-page";

    private static final String PREVIOUS_PAGE = "previous-page";

    private static final String REGIONS = "regions";

    private static final String TITLE = "title";

    private static final String TOTAL_IMAGES = "total-images";

    private static final String TOTAL_PAGES = "total-pages";

    private static final String VALUE = "value";

    @Override
    public void toSAX(final Page<BassettImage> bassettPage, final XMLConsumer xmlConsumer) {
        try {
            if (bassettPage != null) {
                List<BassettImage> bassetts = bassettPage.getContent();
                xmlConsumer.startDocument();
                xmlConsumer.startPrefixMapping("", NAMESPACE);
                XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETTS);
                handlePaging(xmlConsumer, bassettPage);
                for (BassettImage eresource : bassetts) {
                    handleEresource(xmlConsumer, eresource);
                }
                XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETTS);
                xmlConsumer.endPrefixMapping("");
                xmlConsumer.endDocument();
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void handleEresource(final XMLConsumer xmlConsumer, final BassettImage bassett) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(NAMESPACE, BASSETT_NUMBER, BASSETT_NUMBER, "CDATA", bassett.getBassettNumber());
        XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT, attributes);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, TITLE);
        XMLUtils.data(xmlConsumer, bassett.getTitle());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, TITLE);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT_IMAGE);
        XMLUtils.data(xmlConsumer, bassett.getSource());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT_IMAGE);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, DIAGRAM);
        XMLUtils.data(xmlConsumer, bassett.getDiagram());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, DIAGRAM);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, LEGEND_IMAGE);
        XMLUtils.data(xmlConsumer, bassett.getLatinLegend());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, LEGEND_IMAGE);
        if (null != bassett.getEnglishLegend()) {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, LEGEND);
            XMLUtils.data(xmlConsumer, bassett.getEnglishLegend());
            XMLUtils.endElement(xmlConsumer, NAMESPACE, LEGEND);
        }
        if (null != bassett.getDescription()) {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, DESCRIPTION);
            XMLUtils.data(xmlConsumer, bassett.getDescription());
            XMLUtils.endElement(xmlConsumer, NAMESPACE, DESCRIPTION);
        }
        handleRegion(xmlConsumer, bassett);
        XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT);
    }

    private void handlePaging(final XMLConsumer xmlConsumer, final Page<BassettImage> page) throws SAXException {
        handlePagingElement(xmlConsumer, TOTAL_IMAGES, page.getTotalElements());
        handlePagingElement(xmlConsumer, TOTAL_PAGES, page.getTotalPages());
        int currentPage = page.getNumber() + 1;
        int currentImageNumber = page.getNumber() * page.getSize();
        long upCurrentImageNumber = currentImageNumber + (long) page.getNumberOfElements();
        handlePagingElement(xmlConsumer, CURRENT_PAGE, currentPage);
        if (page.isFirst()) {
            handlePagingElement(xmlConsumer, PREVIOUS_PAGE, 1);
        } else {
            handlePagingElement(xmlConsumer, PREVIOUS_PAGE, currentPage - 1);
        }
        if (page.isLast()) {
            handlePagingElement(xmlConsumer, NEXT_PAGE, page.getTotalPages());
        } else {
            handlePagingElement(xmlConsumer, NEXT_PAGE, currentPage + 1);
        }
        handlePagingElement(xmlConsumer, IMAGE_NUMBER_LOW, currentImageNumber + 1);
        handlePagingElement(xmlConsumer, IMAGE_NUMBER_UP, upCurrentImageNumber);
    }

    private void handlePagingElement(final XMLConsumer xmlConsumer, final String name, final Number value)
            throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(NAMESPACE, VALUE, VALUE, "CDATA", String.valueOf(value));
        XMLUtils.startElement(xmlConsumer, NAMESPACE, name, attributes);
        XMLUtils.endElement(xmlConsumer, NAMESPACE, name);
    }

    private void handleRegion(final XMLConsumer xmlConsumer, final BassettImage image) throws SAXException {
        List<String> subregions = image.getSubRegions();
        StringBuilder sb = new StringBuilder();
        if (subregions != null) {
            for (String region : subregions) {
                sb.append(region);
                sb.append(", ");
            }
            String subRegion = sb.toString().trim();
            XMLUtils.startElement(xmlConsumer, NAMESPACE, REGIONS);
            XMLUtils.data(xmlConsumer, sb.substring(0, subRegion.length() - 1).concat("."));
            XMLUtils.endElement(xmlConsumer, NAMESPACE, REGIONS);
        }
    }
}
