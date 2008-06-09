package edu.stanford.irt.laneweb.querymap;

import edu.stanford.irt.querymap.QueryMap;


public interface QueryMapper {
    
    public static final String ROLE = QueryMapper.class.getName();
    
    QueryMap getQueryMap(String query);
}
