package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarksGenerator extends AbstractGenerator {

    private String action;

    private Bookmarks bookmarks;

    private DefaultXMLizableBookmarksView defaultView;

    private boolean editing;
    
    private BookmarksDAO bookmarksDAO;

    private EditingXMLizableBookmarksView editingView;

    private String label;

    private int position;

    private String url;

    public void generate() throws SAXException, IOException {
        if (this.editing) {
            if ("remove".equals(this.action)) {
                this.bookmarks.remove(this.position);
                this.bookmarksDAO.saveBookmarks(this.bookmarks);
            } else if ("add".equals(this.action)) {
                this.bookmarks.add(this.position, (new Bookmark(this.label, this.url)));
                this.bookmarksDAO.saveBookmarks(this.bookmarks);
            }
            this.editingView.toSAX(this.bookmarks, this.xmlConsumer);
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
    
    public void setDefaultView(DefaultXMLizableBookmarksView defaultView) {
        this.defaultView = defaultView;
    }
    
    public void setEditingView(EditingXMLizableBookmarksView editingView) {
        this.editingView = editingView;
    }
    
    public void setBookmarksDAO(BookmarksDAO bookmarksDAO) {
        this.bookmarksDAO = bookmarksDAO;
    }
}
