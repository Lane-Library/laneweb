package edu.stanford.irt.laneweb.eresources;

public interface QueryInspector {

    boolean combinable();

    String inspect(String query);
}
