package edu.stanford.irt.laneweb.history;

public class HistoryPhoto {

    private String page;

    private String thumbnail;

    private String title;

    public HistoryPhoto(final String page, final String thumbnail, final String title) {
        this.page = page;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public String getPage() {
        return this.page;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getTitle() {
        return this.title;
    }
}
