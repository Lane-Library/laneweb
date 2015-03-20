package edu.stanford.irt.laneweb.eresources;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollectionManager {

    List<Eresource> getCore(String type);

    List<Eresource> getMesh(String type, String mesh);

    List<Eresource> getType(String type);

    List<Eresource> getType(String type, char charAt);

    List<Eresource> search(String query);

    Map<String, Integer> searchCount(Set<String> types, String query);

    List<Eresource> searchType(String type, String query);
}
