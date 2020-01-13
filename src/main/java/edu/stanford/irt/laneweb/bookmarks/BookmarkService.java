package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import edu.stanford.irt.status.StatusService;

public interface BookmarkService extends StatusService {

    List<Bookmark> getLinks(String userid);

    int getRowCount();

    void saveLinks(String userid, List<Bookmark> links);
}
