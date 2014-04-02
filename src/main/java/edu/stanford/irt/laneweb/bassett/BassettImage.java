package edu.stanford.irt.laneweb.bassett;

import java.util.ArrayList;
import java.util.List;

public class BassettImage {

    private String bassettNumber;

    private String description;

    private String diagram;

    private String engishLegend;

    private String image;

    private String latinLegend;

    private List<String> regions = new ArrayList<String>();

    private String title;

    public BassettImage(final String description, final String title) {
        this.description = description;
        this.title = title;
    }

    public void addRegion(final String region) {
        this.regions.add(region);
    }

    public String getBassettNumber() {
        return this.bassettNumber;
    }

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

    public String getTitle() {
        return this.title;
    }

    public void setBassettNumber(final String bassettNumber) {
        this.bassettNumber = bassettNumber;
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
        StringBuilder sb = new StringBuilder();
        sb.append("bassettNumber=").append(this.bassettNumber).append(",Image=").append(this.image).append(",Diagram=")
                .append(this.diagram).append(",LatinLegend=").append(this.latinLegend).append(",EngishLegend=")
                .append(this.engishLegend).append(",regions=").append(this.regions);
        return sb.toString();
    }
}
