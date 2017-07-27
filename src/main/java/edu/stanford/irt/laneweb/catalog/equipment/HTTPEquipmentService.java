package edu.stanford.irt.laneweb.catalog.equipment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;

public class HTTPEquipmentService implements EquipmentService {

    private static final String RECORDS_ENDPOINT_PATH = "equipment/records";

    private static final String STATUS_ENDPOINT_PATH_FORMAT = "equipment/status?idList=%s";

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    public HTTPEquipmentService(final ObjectMapper objectMapper, final URI catalogServiceURI) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Override
    public InputStream getRecords(final List<String> params) {
        try {
            return IOUtils.getStream(new URL(this.catalogServiceURI.toURL(), RECORDS_ENDPOINT_PATH));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public List<EquipmentStatus> getStatus(final String idList) {
        String pathWithIDListParam = String.format(STATUS_ENDPOINT_PATH_FORMAT, idList);
        try (InputStream input = IOUtils.getStream(new URL(this.catalogServiceURI.toURL(), pathWithIDListParam))) {
            return this.objectMapper.readValue(input, new TypeReference<List<EquipmentStatus>>() {
            });
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}