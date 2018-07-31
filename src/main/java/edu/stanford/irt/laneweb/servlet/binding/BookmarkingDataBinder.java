package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkingDataBinder implements DataBinder {

    private String bookmarking;

    public BookmarkingDataBinder(final String disasterMode) {
        this.bookmarking = disasterMode == null ? "off" : disasterMode;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.BOOKMARKING, this.bookmarking);
    }
}
