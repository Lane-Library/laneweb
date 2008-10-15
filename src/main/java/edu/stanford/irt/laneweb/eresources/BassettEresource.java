package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.irt.eresources.impl.EresourceImpl;

public class BassettEresource extends EresourceImpl {

    private String bassettNumber;
    private String image;
    private String diagram;
    private String latinLegend;
    private String engishLegend;
    private String description;
    private List<String> regions = new ArrayList<String>();

    public String getBassettNumber() {
        return this.bassettNumber;
    }

    public void setBassettNumber(final String bassettNumber) {
        this.bassettNumber = bassettNumber;
    }

    public String getDiagram() {
        return this.diagram;
    }

    public void setDiagram(final String diagram) {
        this.diagram = diagram;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getEngishLegend() {
        return this.engishLegend;
    }

    public void setEngishLegend(final String engishLegend) {
        this.engishLegend = engishLegend;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getLatinLegend() {
        return this.latinLegend;
    }

    public void setLatinLegend(final String latinLegend) {
        this.latinLegend = latinLegend;
    }

    public List<String> getRegions() {
        return this.regions;
    }

    public void setRegions(final List<String> regions) {
        this.regions = regions;
    }

    public void addRegion(final String region) {
        this.regions.add(region);
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("bassettNumber ---->" + this.bassettNumber);
        sb.append("Image -------->" + this.image);
        sb.append("Diagram ------>" + this.diagram);
        sb.append("LatinLegend -->" + this.latinLegend);
        sb.append("EngishLegend ->" + this.engishLegend);
        sb.append("Region size -->" + this.regions.size());

        for (String region : this.regions) {
            sb.append("region---->" + region);
        }
        return sb.toString();
    }

}
