package edu.stanford.irt.laneweb.flickr;

public class FlickrPhoto {

    private String page;

    private String thumbnail;

    public FlickrPhoto(final String page, final String thumbnail) {
        this.page = page;
        this.thumbnail = thumbnail;
    }

    public String getPage() {
        return this.page;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }
}
