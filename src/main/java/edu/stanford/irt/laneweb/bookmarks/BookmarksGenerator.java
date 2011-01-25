package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarksGenerator extends AbstractGenerator {

    private Bookmarks bookmarks;

    public void generate() throws SAXException, IOException {
        this.bookmarks.toSAX(this.xmlConsumer);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.bookmarks = ModelUtil.getObject(this.model, Model.BOOKMARKS, Bookmarks.class);
    }
}
