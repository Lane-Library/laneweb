package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.util.location.Location;
import org.apache.cocoon.util.location.LocationImpl;
import org.apache.cocoon.util.location.LocationUtils;
import org.apache.cocoon.util.location.LocationUtils.LocationFinder;

import edu.stanford.irt.laneweb.LanewebException;

public class LanewebLocationFinder implements LocationFinder {

    static {
        LocationUtils.addFinder(new LanewebLocationFinder());
    }

    // cut and pasted from AvalonNamespaceHandler which is no longer loaded
    public Location getLocation(final Object obj, final String description) {
        if (obj instanceof Configuration) {
            Configuration config = (Configuration) obj;
            String locString = config.getLocation();
            Location result = LocationUtils.parse(locString);
            if (LocationUtils.isKnown(result)) {
                // Add description
                StringBuffer desc = new StringBuffer().append('<');
                // Unfortunately Configuration.getPrefix() is not public
                try {
                    if (config.getNamespace().startsWith("http://apache.org/cocoon/sitemap/")) {
                        desc.append("map:");
                    }
                } catch (ConfigurationException e) {
                    throw new LanewebException(e);
                }
                desc.append(config.getName()).append('>');
                return new LocationImpl(desc.toString(), result);
            } else {
                return result;
            }
        }
        // Try next finders.
        return null;
    }
}
