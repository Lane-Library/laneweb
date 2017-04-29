package edu.stanford.irt.laneweb.eresources;

@FunctionalInterface
public interface QueryInspector {

    String inspect(String query);
}
