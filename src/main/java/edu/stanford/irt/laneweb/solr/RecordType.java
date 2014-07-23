package edu.stanford.irt.laneweb.solr;

public enum RecordType {
    BIB("Lane Catalog record"), CLASS("Lane Class"), OTHER("Need to enter a new type"), PRINT(""), PUBMED(" - PubMed"), WEB(
            "Web Page");

    private String value = null;

    private RecordType(final String type) {
        this.value = type;
    }

    public String getName() {
        return name();
    }

    public String getvalue() {
        return this.value;
    }
};
