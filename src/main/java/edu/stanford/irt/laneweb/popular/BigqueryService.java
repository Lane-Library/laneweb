package edu.stanford.irt.laneweb.popular;

import java.util.List;
import java.util.Map;

public interface BigqueryService {

    List<Map<String, String>> getAllPopularResources();

    List<Map<String, String>> getPopularResources(String resourceType);
}
