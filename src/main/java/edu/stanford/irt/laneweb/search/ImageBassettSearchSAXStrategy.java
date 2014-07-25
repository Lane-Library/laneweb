package edu.stanford.irt.laneweb.search;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class ImageBassettSearchSAXStrategy extends AbstractImageSearchSAXStrategy<Map<String, Object>> {

    private static final String BASSETT_ICON_SRC_URI = "http://elane.stanford.edu/public/L254573/small/";

    private static final String BASSETT_MEDIUM_IMAGE_SRC = "http://elane.stanford.edu/public/L254573/medium/";

    private static final String BASSETT_PAGE_PATH = "/biomed-resources/bassett/bassettView.html?bn=";

    private static final String BASSETT_SEARCH_URL = "http://lane.stanford.edu/search.html?source=bassett&q=";

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final int MAX_BASSETT_RESULT = 12;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    @SuppressWarnings("unchecked")
    public void toSAX(final Map<String, Object> result, final XMLConsumer xmlConsumer) {
        try {
            String query = (String) result.get(ImageSearchGenerator.SEARCH_TERM);
            List<BassettImage> bassettResult = (List<BassettImage>) result.get(ImageSearchGenerator.BASSETT_RESULT);
            int bassettMaxResult = bassettResult.size() > MAX_BASSETT_RESULT ? MAX_BASSETT_RESULT : bassettResult
                    .size();
            if (!bassettResult.isEmpty()) {
                startDiv(xmlConsumer);
                createTitle(xmlConsumer, "bassett", "Bassett", String.valueOf(bassettMaxResult),
                        String.valueOf(bassettResult.size()), BASSETT_SEARCH_URL.concat(query));
                startElementWithId(xmlConsumer, UL, "imageList");
                for (BassettImage bassettImage : bassettResult) {
                    String titleAndSubtitle = bassettImage.getTitle();
                    String bassettTitle = "";
                    if (titleAndSubtitle != null) {
                        String[] title = titleAndSubtitle.split("\\.");
                        bassettTitle = title[0];
                    }
                    generateImages(xmlConsumer, bassettImage.getBassettNumber(), bassettTitle,
                            BASSETT_PAGE_PATH.concat(bassettImage.getBassettNumber()),
                            BASSETT_ICON_SRC_URI.concat(bassettImage.getImage()), bassettImage.getImage());
                    if (--bassettMaxResult == 0) {
                        break;
                    }
                }
                endUl(xmlConsumer);
                endDiv(xmlConsumer);
                generateTooltips(xmlConsumer, bassettResult);
            }
        } catch (SAXException | ParseException e) {
            throw new LanewebException(e);
        }
    }

    protected void generateTooltips(final XMLConsumer xmlConsumer, final List<BassettImage> bassettImages)
            throws SAXException, ParseException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, "style", "style", CDATA, "display:none");
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "tooltips");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        int bassettMaxResult = bassettImages.size() > MAX_BASSETT_RESULT ? MAX_BASSETT_RESULT : bassettImages.size();
        for (BassettImage bassettImage : bassettImages) {
            generateTooltipsImage(xmlConsumer, bassettImage.getBassettNumber(),
                    BASSETT_MEDIUM_IMAGE_SRC.concat(bassettImage.getImage()));
            if (--bassettMaxResult == 0) {
                break;
            }
        }
        endDiv(xmlConsumer);
    }
}
