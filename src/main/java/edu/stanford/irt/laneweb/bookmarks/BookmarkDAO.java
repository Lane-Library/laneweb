package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

public interface BookmarkDAO {

    List<Bookmark> getLinks(String sunetid);

    void saveLinks(String sunetid, List<Bookmark> links);
}