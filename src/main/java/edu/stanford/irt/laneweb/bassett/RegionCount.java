package edu.stanford.irt.laneweb.bassett;

import java.util.List;

public class RegionCount {

    private int count;

    private String name;

    private List<RegionCount> subRegionCounts;

    public RegionCount(final String name, final int count) {
        this.name = name;
        this.count = count;
    }

    public RegionCount(final String name, final int count, final List<RegionCount> subRegionCounts) {
        this.name = name;
        this.count = count;
        this.subRegionCounts = subRegionCounts;
    }

    protected int getCount() {
        return this.count;
    }

    protected String getRegionName() {
        return this.name;
    }

    protected List<RegionCount> getSubRegionCounts() {
        return this.subRegionCounts;
    }
}
