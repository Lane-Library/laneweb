package edu.stanford.irt.laneweb.eresources.browse;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;

public class LinkWithoutCoverEresourceSAXStrategy extends AbstractXHTMLSAXStrategy<Eresource> {

    @Override
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            String recId = eresource.getRecordId();
            String href = eresource.getLinks().stream().findFirst()
                    .orElseThrow(() -> new LanewebException("no link for eresource " + recId)).getUrl();
            String title = TitleNormalizer.toTitleCase(eresource.getTitle());
            createAnchorWithTitle(xmlConsumer, href, title, title);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
