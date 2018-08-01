package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import edu.stanford.irt.status.ApplicationStatus;

public interface BookmarkService {

    List<Bookmark> getLinks(String userid) throws BookmarkException;

    int getRowCount() throws BookmarkException;

    ApplicationStatus getStatus();

    void saveLinks(String userid, List<Bookmark> links) throws BookmarkException;
}
