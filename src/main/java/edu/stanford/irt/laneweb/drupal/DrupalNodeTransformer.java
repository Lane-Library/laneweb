package edu.stanford.irt.laneweb.drupal;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class DrupalNodeTransformer extends AbstractXMLPipe implements Transformer {

    private DrupalAPIService apiService;

    private XMLConsumer xmlConsumer;

    public DrupalNodeTransformer(final DrupalAPIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        if ("drupal".equals(target)) {
            XMLUtils.createElement(this.xmlConsumer, "http://www.w3.org/1999/xhtml", "drupal", new AttributesImpl(),
                    this.apiService.getNodeContent(data));
        } else {
            super.processingInstruction(target, data);
        }
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }
}
