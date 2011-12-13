package edu.stanford.irt.laneweb.personalize;

import java.util.List;

import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkGenerator extends AbstractMarshallingGenerator {

//    private String action;

    private List<Bookmark> bookmarks;

//    private BookmarkDAO<Bookmark> bookmarksDAO;
//
//    private boolean editing;
//
//    private String label;
//
//    private int position;
//
//    private String sunetid;
//
//    private String url;

    public void generate() {
//        if (this.editing) {
//            if ("remove".equals(this.action)) {
//                this.bookmarks.remove(this.position);
//                this.bookmarksDAO.saveLinks(this.sunetid, this.bookmarks);
//            } else if ("add".equals(this.action)) {
//                this.bookmarks.add(this.position, (new Bookmark(this.label, this.url)));
//                this.bookmarksDAO.saveLinks(this.sunetid, this.bookmarks);
//            }
//        }
        marshall(this.bookmarks);
    }

//    public void setBookmarksDAO(final BookmarkDAO<Bookmark> bookmarksDAO) {
//        this.bookmarksDAO = bookmarksDAO;
//    }

    @Override
    protected void initialize() {
        super.initialize();
        this.bookmarks = ModelUtil.getObject(this.model, Model.BOOKMARKS, List.class);
//        this.editing = ModelUtil.getString(this.model, Model.REQUEST_URI).endsWith("edit-bookmarks.html");
//        this.position = Integer.parseInt(ModelUtil.getString(this.model, "bookmarks:position", "0"));
//        this.action = ModelUtil.getString(this.model, "bookmarks:action");
//        this.label = ModelUtil.getString(this.model, "bookmarks:label");
//        this.url = ModelUtil.getString(this.model, "bookmarks:url");
//        this.sunetid = ModelUtil.getString(this.model, Model.SUNETID);
    }
}
