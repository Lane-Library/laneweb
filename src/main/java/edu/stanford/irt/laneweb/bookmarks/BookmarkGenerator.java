package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkGenerator extends AbstractMarshallingGenerator {

    private List<Bookmark> bookmarks;

    public void generate() {
        marshall(this.bookmarks);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.bookmarks = ModelUtil.getObject(getModel(), Model.BOOKMARKS, List.class);
    }
}
