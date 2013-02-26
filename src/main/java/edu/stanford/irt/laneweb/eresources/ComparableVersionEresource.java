package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.eresources.impl.EresourceImpl;

/**
 * An Eresource that contains an ordered Set of Versions
 */
public class ComparableVersionEresource extends EresourceImpl {

    private Set<Version> versions;

    /**
     * add a Version
     * 
     * @param version
     *            the Version to add
     */
    @Override
    public void addVersion(final Version version) {
        if (this.versions == null) {
            this.versions = new TreeSet<Version>();
        }
        this.versions.add(version);
    }

    /**
     * get the Versions
     * 
     * @return and unmodifiable Set of Versions
     */
    @Override
    public Collection<Version> getVersions() {
        if (this.versions == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(this.versions);
    }
}
