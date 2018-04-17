package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private List<Bookmark> bookmarks;

    public BookmarkGenerator(final Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    public void doGenerate(final XMLConsumer xmlConsumer) {
        marshal(this.bookmarks, xmlConsumer);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.bookmarks = ModelUtil.getObject(model, Model.BOOKMARKS, List.class);
    }
}
