package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

public interface BookmarkDAO<T extends Bookmark> {

    List<T> getLinks(String sunetid);

    void saveLinks(String sunetid, List<T> links);
}