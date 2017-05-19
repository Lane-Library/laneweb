package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

public interface BookmarkService {

    List<Bookmark> getLinks(String userid);

    int getRowCount();

    void saveLinks(String userid, List<Bookmark> links);
}