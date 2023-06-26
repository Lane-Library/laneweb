package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FacetFieldEntry implements Serializable {

    private static final long serialVersionUID = 5119677721013317629L;

    private int valueCount;

    private String value;

    private Field field;

    public FacetFieldEntry() {
        this.value = null;
        this.valueCount = 0;
    }

    public FacetFieldEntry(Field field, String value, int count) {
        this.value = value;
        this.valueCount = count;
        setField(field);
    }

    public int getValueCount() {
        return valueCount;
    }

    public void setValueCount(int valueCount) {
        this.valueCount = valueCount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @JsonIgnore
    public Field getKey() {
        return getField();
    }

}
