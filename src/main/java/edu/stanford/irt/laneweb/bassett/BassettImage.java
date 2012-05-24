package edu.stanford.irt.laneweb.bassett;

import java.util.List;
import java.util.Map;

public class BassettImage {

    private String bassettImage;

    private String bassettNumber;

    private String description;

    private String diagramImage;

    private String engishLegend;

    private String latinLegend;

    private Map<String, List<String>> regions;

    private String title;

    public String getBassettNumber() {
        return this.bassettNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDiagram() {
        return this.diagramImage;
    }

    public String getEngishLegend() {
        return this.engishLegend;
    }

    public String getImage() {
        return this.bassettImage;
    }

    public String getLatinLegend() {
        return this.latinLegend;
    }

    public Map<String, List<String>> getRegions() {
        return this.regions;
    }

    public String getTitle() {
        return this.title;
    }

    public void setBassettNumber(final String bassettNumber) {
        this.bassettNumber = bassettNumber;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setDiagram(final String diagram) {
        this.diagramImage = diagram;
    }

    public void setEngishLegend(final String engishLegend) {
        this.engishLegend = engishLegend;
    }

    public void setImage(final String image) {
        this.bassettImage = image;
    }

    public void setLatinLegend(final String latinLegend) {
        this.latinLegend = latinLegend;
    }

    public void setRegions(final Map<String, List<String>> regions) {
        this.regions = regions;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("bassettNumber ---->" + this.bassettNumber);
        sb.append("Image -------->" + this.bassettImage);
        sb.append("Diagram ------>" + this.diagramImage);
        sb.append("LatinLegend -->" + this.latinLegend);
        sb.append("EngishLegend ->" + this.engishLegend);
        return sb.toString();
    }
}
