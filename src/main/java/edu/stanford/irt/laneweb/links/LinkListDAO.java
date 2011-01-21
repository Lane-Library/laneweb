package edu.stanford.irt.laneweb.links;

import java.util.List;

import edu.stanford.irt.eresources.Link;

public interface LinkListDAO {

    public LinkList getLinks(final String sunetid);

    public void saveLinks(final String sunetid, final List<Link> links);
}