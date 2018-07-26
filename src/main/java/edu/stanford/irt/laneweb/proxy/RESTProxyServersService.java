package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTProxyServersService implements ProxyServersService {

    private static final int BYTE_BUFFER_SIZE = 4096;

    private static final String HOSTS_ENDPOINT = "proxy/hosts";

    private static final String WRITE_ENDPOINT = "proxy/write";

    private URI catalogServiceURI;

    private RESTService restservice;

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
        try (InputStream input = this.restservice.getInputStream(this.catalogServiceURI.resolve(WRITE_ENDPOINT))) {
            byte[] buffer = new byte[BYTE_BUFFER_SIZE];
            int i = 0;
            while (true) {
                i = input.read(buffer);
                if (i == -1) {
                    break;
                }
                outputStream.write(buffer, 0, i);
            }
        }
    }
}
