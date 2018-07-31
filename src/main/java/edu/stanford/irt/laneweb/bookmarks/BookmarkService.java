package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

public interface BookmarkService {

    List<Bookmark> getLinks(String userid) throws BookmarkException;

    int getRowCount() throws BookmarkException;

    void saveLinks(String userid, List<Bookmark> links) throws BookmarkException;
}
