package edu.stanford.irt.laneweb.voyager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.ServiceURIResolver;

public class HTTPLoginService implements LoginService {

    private static final String ENDPOINT_PATH_FORMAT = "login?univid=%s&pid=%s";

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    private ServiceURIResolver uriResolver;

    public HTTPLoginService(final ObjectMapper objectMapper, final URI catalogServiceURI,
            final ServiceURIResolver uriResolver) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
        this.uriResolver = uriResolver;
    }

    @Override
    public boolean login(final String voyagerUnivId, final String pid) {
        String endpointPath = String.format(ENDPOINT_PATH_FORMAT, voyagerUnivId, pid);
        try (InputStream input = this.uriResolver.getInputStream(this.catalogServiceURI.resolve(endpointPath))) {
            return this.objectMapper.readValue(input, Boolean.class).booleanValue();
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
