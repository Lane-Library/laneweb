package edu.stanford.irt.laneweb.catalog.equipment;

public class Equipment {

    private String bibID;

    private String count;

    private String note;

    private String title;

    public Equipment(final String bibID, final String count, final String note, final String title) {
        this.bibID = bibID;
        this.count = count;
        this.note = note;
        this.title = title;
    }

    public String getBibID() {
        return this.bibID;
    }

    public String getCount() {
        return this.count;
    }

    public String getNote() {
        return this.note;
    }

    public String getTitle() {
        return this.title;
    }
}
