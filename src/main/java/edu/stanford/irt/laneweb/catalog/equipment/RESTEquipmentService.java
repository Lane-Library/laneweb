package edu.stanford.irt.laneweb.catalog.equipment;

import java.net.URI;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTEquipmentService implements EquipmentService {

    private static final String LIST_ENDPOINT_PATH = "equipment/list";

    private static final TypeReference<List<Map<String, String>>> TYPE = new TypeReference<>() {
    };

    private URI catalogServiceURI;

    private BasicAuthRESTService restService;

    public RESTEquipmentService(final URI catalogServiceURI, final BasicAuthRESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public List<Equipment> getList() {
        List<Map<String, String>> list = this.restService.getObject(this.catalogServiceURI.resolve(LIST_ENDPOINT_PATH),
                TYPE);
        return list.stream().map(m -> new Equipment(m.get("bibID"), m.get("count"), m.get("note"), m.get("title")))
                .toList();
    }
}
