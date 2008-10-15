package edu.stanford.irt.laneweb.querymap;

import java.util.Map;
import java.util.Set;

import edu.stanford.irt.querymap.QueryMap;

public interface QueryMapper {

    public static final String ROLE = QueryMapper.class.getName();

    QueryMap getQueryMap(String query);

    QueryMap getQueryMap(String query, Map<String, Set<String>> resourceMaps, Map<String, Float> descriptorWeights, int abstractCount);
}
