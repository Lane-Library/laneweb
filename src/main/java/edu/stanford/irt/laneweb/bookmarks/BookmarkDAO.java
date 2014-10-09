package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

public interface BookmarkDAO {

    List<Object> getLinks(String userid);

    int getRowCount();

    void saveLinks(String userid, List<Object> links);
}