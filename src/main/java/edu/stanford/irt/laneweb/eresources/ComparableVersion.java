package edu.stanford.irt.laneweb.eresources;

import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.eresources.impl.VersionImpl;

/**
 * This Version implementation implements Comparable so that when added to a Set Versions are ordered as determined by a
 * EresourceVersionComparator.
 */
public class ComparableVersion extends VersionImpl implements Comparable<Version> {

    private static final EresourceVersionComparator COMPARATOR = new EresourceVersionComparator();

    /**
     * Uses a EresourceVersionComparator to compare this Version to another.
     * 
     * @see {@link java.lang.Comparable.compareTo}
     */
    public int compareTo(final Version o) {
        return COMPARATOR.compare(this, o);
    }
}
