package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

/**
 * This class is a wrapper for the SQLBookmarkDAO to provide backwards compatibility for the bookmarks table by removing
 * {@literal @}stanford.edu from the userid for the database queries.
 */
public class StanfordDomainStrippingBookmarkDAO implements BookmarkDAO {

    private static final String AT_STANFORD_EDU = "@stanford.edu";

    private BookmarkDAO bookmarkDAO;

    public StanfordDomainStrippingBookmarkDAO(final BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }

    @Override
    public List<Bookmark> getLinks(final String userid) {
        return this.bookmarkDAO.getLinks(stripStanfordDomain(userid));
    }

    @Override
    public int getRowCount() {
        return this.bookmarkDAO.getRowCount();
    }

    @Override
    public void saveLinks(final String userid, final List<Bookmark> links) {
        this.bookmarkDAO.saveLinks(stripStanfordDomain(userid), links);
    }

    private String stripStanfordDomain(final String userid) {
        int index = userid.indexOf(AT_STANFORD_EDU);
        if (index == -1) {
            return userid;
        }
        return userid.substring(0, index);
    }
}
