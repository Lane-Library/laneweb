package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTProxyServersService implements ProxyServersService {

    private static final String HOSTS_ENDPOINT = "proxy/hosts";

    public RESTService restservice;

    private URI catalogServiceURI;

    public RESTProxyServersService(final URI catalogServiceURI, final RESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restservice = restService;
    }

    @Override
    public Set<String> getHosts() {
        URI uri = this.catalogServiceURI.resolve(HOSTS_ENDPOINT);
        return this.restservice.getObject(uri, Set.class);
    }

    @Override
    public void write(final OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException();
    }
}
