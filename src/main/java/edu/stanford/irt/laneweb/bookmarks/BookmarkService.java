package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import edu.stanford.irt.status.StatusService;

public interface BookmarkService extends StatusService {

    List<Bookmark> getLinks(String userid) throws BookmarkException;

    int getRowCount() throws BookmarkException;

    void saveLinks(String userid, List<Bookmark> links) throws BookmarkException;
}
