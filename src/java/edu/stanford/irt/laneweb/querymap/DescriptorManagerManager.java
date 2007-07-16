/**
 * 
 */
package edu.stanford.irt.laneweb.querymap;

import edu.stanford.irt.querymap.DescriptorManager;

/**
 * @author ceyates
 * 
 */
public interface DescriptorManagerManager {
	
	public static final String ROLE = DescriptorManagerManager.class.getName();

	DescriptorManager getDescriptorManager();

}
