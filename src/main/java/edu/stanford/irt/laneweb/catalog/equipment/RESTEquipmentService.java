package edu.stanford.irt.laneweb.catalog.equipment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;
import edu.stanford.irt.laneweb.util.ServiceURIResolver;


public class RESTEquipmentService implements EquipmentService {

    private static final TypeReference<List<Map<String, String>>> TYPE = new TypeReference<List<Map<String, String>>>() {
    };

    private static final String RECORDS_ENDPOINT_PATH = "equipment/records";

    private static final String STATUS_ENDPOINT_PATH_FORMAT = "equipment/status?idList=%s";

    private URI catalogServiceURI;
    private RESTService restService;

    private ServiceURIResolver uriResolver;

    public RESTEquipmentService(URI catalogServiceURI, RESTService restService, ServiceURIResolver uriResolver) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
        this.uriResolver = uriResolver;
    }

    @Override
    public InputStream getRecords(List<String> params) {
        try {
            return this.uriResolver.getInputStream(this.catalogServiceURI.resolve(RECORDS_ENDPOINT_PATH));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public List<EquipmentStatus> getStatus(String idList) {
        String pathWithIDListParam = String.format(STATUS_ENDPOINT_PATH_FORMAT, idList);
        List<Map<String, String>> list = this.restService.getObject(this.catalogServiceURI.resolve(pathWithIDListParam), TYPE);
        return list.stream()
                .map(m -> new EquipmentStatus(m.get("bibID"), m.get("count")))
                .collect(Collectors.toList());
    }
}
