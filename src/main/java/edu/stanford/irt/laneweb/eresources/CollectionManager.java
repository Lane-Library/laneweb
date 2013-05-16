package edu.stanford.irt.laneweb.eresources;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface CollectionManager {

    List<Eresource> getCore(String type);

    List<Eresource> getMesh(String type, String mesh);

    List<Eresource> getSubset(String subset);

    List<Eresource> getType(String type, char charAt);

    List<Eresource> getType(String type);

    List<Eresource> searchType(String type, String query);

    Map<String, Integer> searchCount(Set<String> types, Set<String> subsets, String query);

    List<Eresource> search(String query);
}
