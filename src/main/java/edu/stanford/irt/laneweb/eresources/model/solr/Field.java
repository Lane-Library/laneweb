package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;

import org.springframework.util.ObjectUtils;

public class Field implements Serializable {

    private static final long serialVersionUID = 1505057154688260997L;

    private final String name;

    public Field() {
        this.name = null;
    }

    public Field(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Field)) {
            return false;
        }
        Field that = (Field) other;
        return ObjectUtils.nullSafeEquals(this.name, that.name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
