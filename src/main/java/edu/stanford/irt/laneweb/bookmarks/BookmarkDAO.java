package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

public interface BookmarkDAO {

    List<Object> getLinks(String sunetid);

    int getRowCount();

    void saveLinks(String sunetid, List<Object> links);
}