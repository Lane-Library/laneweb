package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;

import org.springframework.util.ObjectUtils;

public class Field implements Serializable {
    

    private static final long serialVersionUID = 1505057154688260997L;

   

    private final String name;

    public Field(String name) {
        this.name = name;
    }

    public Field() {
        this.name = null;
    }

  
    public String getName() {
        return this.name;
    }

   
    public String toString() {
        return this.name;
    }

   
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.name);
    }

   
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Field)) {
            return false;
        }
        Field that = (Field) other;
        return ObjectUtils.nullSafeEquals(this.name, that.name);
    }

}

