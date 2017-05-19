package edu.stanford.irt.laneweb.catalog.equipment;

public class EquipmentStatus {

    private String bibID;

    private String count;

    public EquipmentStatus(final String bibID, final String count) {
        this.bibID = bibID;
        this.count = count;
    }

    public String getBibID() {
        return this.bibID;
    }

    public String getCount() {
        return this.count;
    }
}
