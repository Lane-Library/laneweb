package edu.stanford.irt.laneweb.links;

import java.io.IOException;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.HTMLGenerator;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class LinkListGenerator extends HTMLGenerator {

    private LinkList links;

    @Override
    public void generate() throws SAXException, IOException {
        if (this.links == null) {
            super.generate();
        } else {
            this.links.toSAX(this.xmlConsumer);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.links = ModelUtil.getObject(this.model, "link-list", LinkList.class);
    }
}
