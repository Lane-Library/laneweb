package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.HTMLGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarksGenerator extends HTMLGenerator {

    private Bookmarks bookmarks;

    @Override
    public void generate() throws SAXException, IOException {
        if (this.bookmarks == null) {
            super.generate();
        } else {
            this.bookmarks.toSAX(this.xmlConsumer);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.bookmarks = ModelUtil.getObject(this.model, Model.BOOKMARKS, Bookmarks.class);
    }
}
