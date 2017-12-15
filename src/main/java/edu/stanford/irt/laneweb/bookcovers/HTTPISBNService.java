package edu.stanford.irt.laneweb.bookcovers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.bookcovers.ISBNService;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;

public class HTTPISBNService implements ISBNService {

    private static final String ISBN_ENDPOINT = "isbn?";

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    public HTTPISBNService(final ObjectMapper objectMapper, final URI catalogServiceURI) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Override
    public Map<Integer, List<String>> getISBNs(final List<Integer> bibids) {
        StringBuilder queryStringBuilder = new StringBuilder(ISBN_ENDPOINT);
        bibids.stream()
            .forEach((final Integer bibid) -> queryStringBuilder.append("bibID=").append(bibid).append('&'));
        try (InputStream input = IOUtils
                .getStream(this.catalogServiceURI.resolve(queryStringBuilder.toString()))) {
            return this.objectMapper.readValue(input, new TypeReference<Map<Integer, List<String>>>() {
            });
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
