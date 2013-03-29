package edu.stanford.irt.laneweb.bassett;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.irt.eresources.impl.EresourceImpl;

public class BassettEresource extends EresourceImpl {

    private String bassettNumber;

    private String description;

    private String diagram;

    private String engishLegend;

    private String image;

    private String latinLegend;

    private List<String> regions = new ArrayList<String>();

    public void addRegion(final String region) {
        this.regions.add(region);
    }

    public String getBassettNumber() {
        return this.bassettNumber;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getDiagram() {
        return this.diagram;
    }

    public String getEngishLegend() {
        return this.engishLegend;
    }

    public String getImage() {
        return this.image;
    }

    public String getLatinLegend() {
        return this.latinLegend;
    }

    public List<String> getRegions() {
        return this.regions;
    }

    public void setBassettNumber(final String bassettNumber) {
        this.bassettNumber = bassettNumber;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    public void setDiagram(final String diagram) {
        this.diagram = diagram;
    }

    public void setEngishLegend(final String engishLegend) {
        this.engishLegend = engishLegend;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public void setLatinLegend(final String latinLegend) {
        this.latinLegend = latinLegend;
    }

    public void setRegions(final List<String> regions) {
        this.regions = regions;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("bassettNumber=").append(this.bassettNumber)
        .append(",Image=").append(this.image)
        .append(",Diagram=").append(this.diagram)
        .append(",LatinLegend=").append(this.latinLegend)
        .append(",EngishLegend=").append(this.engishLegend)
        .append(",regions=").append(this.regions);
        return sb.toString();
    }
}
