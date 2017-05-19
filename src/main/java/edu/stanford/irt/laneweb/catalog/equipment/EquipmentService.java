package edu.stanford.irt.laneweb.catalog.equipment;

import java.util.List;

import edu.stanford.irt.laneweb.catalog.CatalogRecordService;

public interface EquipmentService extends CatalogRecordService {

    List<EquipmentStatus> getStatus(String idList);
}
