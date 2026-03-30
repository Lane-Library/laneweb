package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;

public class FacetFieldEntry implements Serializable {

    private static final long serialVersionUID = 5119677721013317629L;

    private String fieldName;

    private String value;

    private int valueCount;

    public FacetFieldEntry() {
        this.value = null;
        this.valueCount = 0;
    }

    public FacetFieldEntry(final String fieldName, final String value, final int count) {
        this.fieldName = fieldName;
        this.value = value;
        this.valueCount = count;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getValue() {
        return this.value;
    }

    public int getValueCount() {
        return this.valueCount;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void setValueCount(final int valueCount) {
        this.valueCount = valueCount;
    }
}
