package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface CollectionManager {

    Collection<Eresource> getCore(String type);

    Collection<Eresource> getMesh(String type, String mesh);

    Collection<Eresource> getSubset(String subset);

    Collection<Eresource> getType(String type, char charAt);

    Collection<Eresource> getType(String type);

    Collection<Eresource> searchType(String type, String query);

    Map<String, Integer> searchCount(Set<String> types, Set<String> subsets, String query);

    Collection<Eresource> search(String query);
}
