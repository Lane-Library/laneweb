package edu.stanford.irt.laneweb.eresources.browse;

import java.util.List;
import java.util.Locale;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class AtoZBrowseSAXStrategy extends AbstractXHTMLSAXStrategy<List<BrowseLetter>> {

    @Override
    public void toSAX(final List<BrowseLetter> letters, final XMLConsumer xmlConsumer) {
        try {
            startUlWithClass(xmlConsumer, "browseTabs");
            for (BrowseLetter letter : letters) {
                startLi(xmlConsumer);
                if (letter.getCount() > 0 && "#".equals(letter.getLetter())) {
                    startAnchorWithTitle(xmlConsumer, letter.getUrl(), "non-alphabetical characters");
                } else if (letter.getCount() > 0) {
                    startAnchor(xmlConsumer, letter.getUrl());
                } else {
                    startAnchorWithClass(xmlConsumer, letter.getUrl(), "disabled");
                }
                XMLUtils.data(xmlConsumer, letter.getLetter().toUpperCase(Locale.US));
                endAnchor(xmlConsumer);
                endLi(xmlConsumer);
            }
            endUl(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
