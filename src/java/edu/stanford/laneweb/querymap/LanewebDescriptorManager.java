/**
 * 
 */
package edu.stanford.laneweb.querymap;

import edu.stanford.irt.querymap.DescriptorManager;

/**
 * @author ceyates
 *
 */
public class LanewebDescriptorManager implements DescriptorManagerManager {
	
	edu.stanford.irt.querymap.DescriptorManager manager;
	
	public LanewebDescriptorManager() {
		this.manager = new edu.stanford.irt.querymap.DescriptorManager();
	}

	public DescriptorManager getDescriptorManager() {
		return this.manager;
	}

}
