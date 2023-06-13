package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkingDataBinder implements DataBinder {

    private String bookmarking;

    public BookmarkingDataBinder(final String bookmarkingMode) {
        this.bookmarking = bookmarkingMode == null ? "off" : bookmarkingMode;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.BOOKMARKING, this.bookmarking);
    }
}
