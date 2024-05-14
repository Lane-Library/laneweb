package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FacetFieldEntry implements Serializable {

    private static final long serialVersionUID = 5119677721013317629L;

    private Field field;

    private String value;

    private int valueCount;

    public FacetFieldEntry() {
        this.value = null;
        this.valueCount = 0;
    }

    public FacetFieldEntry(final Field field, final String value, final int count) {
        this.value = value;
        this.valueCount = count;
        setField(field);
    }

    public Field getField() {
        return this.field;
    }

    @JsonIgnore
    public Field getKey() {
        return getField();
    }

    public String getValue() {
        return this.value;
    }

    public int getValueCount() {
        return this.valueCount;
    }

    public void setField(final Field field) {
        this.field = field;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void setValueCount(final int valueCount) {
        this.valueCount = valueCount;
    }
}
