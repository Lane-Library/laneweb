package edu.stanford.irt.laneweb.bookmarks;

import java.net.URI;
import java.util.List;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTBookmarkService implements BookmarkService {

    private static final TypeReference<List<Bookmark>> TYPE = new TypeReference<List<Bookmark>>() {
    };

    private URI bookmarksURI;

    private RESTService restService;

    private URI rowCountURI;

    public RESTBookmarkService(final URI bookmarksURI, final RESTService restService) {
        this.bookmarksURI = bookmarksURI;
        this.restService = restService;
        this.rowCountURI = bookmarksURI.resolve("rowCount");
    }

    @Override
    public List<Bookmark> getLinks(final String userid) {
        try {
            return this.restService.getObject(this.bookmarksURI.resolve(userid), TYPE);
        } catch (RESTException e) {
            throw new BookmarkException(e);
        }
    }

    @Override
    public int getRowCount() {
        try {
            return this.restService.getObject(this.rowCountURI, Integer.class).intValue();
        } catch (RESTException e) {
            throw new BookmarkException(e);
        }
    }

    @Override
    public ApplicationStatus getStatus() {
        URI uri = this.bookmarksURI.resolve("status.json");
        return this.restService.getObject(uri, ApplicationStatus.class);
    }

    @Override
    public void saveLinks(final String userid, final List<Bookmark> links) {
        try {
            this.restService.putObject(this.bookmarksURI.resolve(userid), links);
        } catch (RESTException e) {
            throw new BookmarkException(e);
        }
    }
}
