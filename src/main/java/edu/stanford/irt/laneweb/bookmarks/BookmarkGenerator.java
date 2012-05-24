package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private List<Bookmark> bookmarks;

    @Override
    public void doGenerate(final XMLConsumer xmlConsumer) {
        marshall(this.bookmarks, xmlConsumer);
    }

    @SuppressWarnings("unchecked")
    public void setModel(final Map<String, Object> model) {
        this.bookmarks = ModelUtil.getObject(model, Model.BOOKMARKS, List.class);
    }
}
