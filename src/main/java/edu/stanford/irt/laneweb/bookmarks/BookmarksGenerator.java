package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;

import javax.annotation.Resource;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarksGenerator extends AbstractGenerator {

    private String action;

    private Bookmarks bookmarks;

    @Resource
    private BookmarksController controller;

    @Resource(name = "defaultXMLizableBookmarksView")
    private DefaultXMLizableBookmarksView defaultView;

    private boolean editing;

    @Resource
    private EditingXMLizableBookmarksView editingView;

    private String label;

    private int position;

    private String url;

    public void generate() throws SAXException, IOException {
        if (this.editing) {
            if ("delete".equals(this.action)) {
                this.controller.deleteBookmark(this.position, this.bookmarks);
//            } else if ("up".equals(this.action)) {
//                this.controller.moveUp(this.position, this.bookmarks);
//            } else if ("down".equals(this.action)) {
//                this.controller.moveDown(this.position, this.bookmarks);
            } else if ("add".equals(this.action)) {
                this.controller.addBookmark(this.label, this.url, this.bookmarks);
            }
            // if ("insertBefore".equals(this.action) ||
            // "insertAfter".equals(this.action)) {
            // int formPosition = "insertBefore".equals(this.action) ?
            // this.position : this.position + 1;
            // this.editingView.toSAX(this.bookmarks, this.xmlConsumer,
            // formPosition);
            // } else {
            this.editingView.toSAX(this.bookmarks, this.xmlConsumer);
            // }
        } else {
            this.defaultView.toSAX(this.bookmarks, this.xmlConsumer);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.bookmarks = ModelUtil.getObject(this.model, Model.BOOKMARKS, Bookmarks.class);
        this.editing = ModelUtil.getString(this.model, Model.REQUEST_URI).endsWith("edit-bookmarks.html");
        this.position = Integer.parseInt(ModelUtil.getString(this.model, "bookmarks:position", "0"));
        this.action = ModelUtil.getString(this.model, "bookmarks:action");
        this.label = ModelUtil.getString(this.model, "bookmarks:label");
        this.url = ModelUtil.getString(this.model, "bookmarks:url");
    }
}
