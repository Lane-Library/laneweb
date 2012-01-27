package edu.stanford.irt.laneweb.bookmarks;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

public class BookmarksEnabledSelector implements Selector {

    private boolean enabled;

    public BookmarksEnabledSelector(final boolean enabled) {
        this.enabled = enabled;
    }

    @SuppressWarnings("rawtypes")
    public boolean select(final String expression, final Map objectModel, final Parameters parameters) {
        return this.enabled;
    }
}
