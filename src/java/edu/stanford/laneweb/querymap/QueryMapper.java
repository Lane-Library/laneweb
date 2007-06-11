/**
 * 
 */
package edu.stanford.laneweb.querymap;

import edu.stanford.irt.querymap.QueryMap;

/**
 * @author ceyates
 *
 */
public interface QueryMapper {

	public static final String ROLE = QueryMapper.class.getName();
	
	QueryMap getQueryMap(String query);

}
