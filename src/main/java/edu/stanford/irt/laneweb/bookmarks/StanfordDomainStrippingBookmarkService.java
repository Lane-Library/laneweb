package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import edu.stanford.irt.status.ApplicationStatus;

/**
 * This class is a wrapper for the JDBCBookmarkService to provide backwards compatibility for the bookmarks table by
 * removing {@literal @}stanford.edu from the userid for the database queries.
 */
public class StanfordDomainStrippingBookmarkService implements BookmarkService {

    private static final String AT_STANFORD_EDU = "@stanford.edu";

    private BookmarkService bookmarkService;

    public StanfordDomainStrippingBookmarkService(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Override
    public List<Bookmark> getLinks(final String userid) {
        return this.bookmarkService.getLinks(stripStanfordDomain(userid));
    }

    @Override
    public int getRowCount() {
        return this.bookmarkService.getRowCount();
    }

    @Override
    public ApplicationStatus getStatus() {
        return this.bookmarkService.getStatus();
    }

    @Override
    public void saveLinks(final String userid, final List<Bookmark> links) {
        this.bookmarkService.saveLinks(stripStanfordDomain(userid), links);
    }

    private String stripStanfordDomain(final String userid) {
        int index = userid.indexOf(AT_STANFORD_EDU);
        if (index == -1) {
            return userid;
        }
        return userid.substring(0, index);
    }
}
