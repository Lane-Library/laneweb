package edu.stanford.irt.laneweb.catalog.equipment;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTEquipmentService implements EquipmentService {

    private static final String RECORDS_ENDPOINT_PATH = "equipment/records";

    private static final String STATUS_ENDPOINT_PATH_FORMAT = "equipment/status?idList=%s";

    private static final TypeReference<List<Map<String, String>>> TYPE =
            new TypeReference<List<Map<String, String>>>() {};

    private URI catalogServiceURI;

    private BasicAuthRESTService restService;

    public RESTEquipmentService(final URI catalogServiceURI, final BasicAuthRESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public InputStream getRecords(final List<String> params) {
        return this.restService.getInputStream(this.catalogServiceURI.resolve(RECORDS_ENDPOINT_PATH));
    }

    @Override
    public List<EquipmentStatus> getStatus(final String idList) {
        String pathWithIDListParam = String.format(STATUS_ENDPOINT_PATH_FORMAT, idList);
        List<Map<String, String>> list = this.restService.getObject(this.catalogServiceURI.resolve(pathWithIDListParam),
                TYPE);
        return list.stream()
                .map(m -> new EquipmentStatus(m.get("bibID"), m.get("count")))
                .collect(Collectors.toList());
    }
}
