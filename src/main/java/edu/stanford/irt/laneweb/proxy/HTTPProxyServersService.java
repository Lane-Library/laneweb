package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;

public class HTTPProxyServersService implements ProxyServersService {

    private static final int BYTE_BUFFER_SIZE = 4096;

    private static final String HOSTS_ENDPOINT = "proxy/hosts";

    private static final String WRITE_ENDPOINT = "proxy/write";

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    public HTTPProxyServersService(final ObjectMapper objectMapper, final URI catalogServiceURI) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Override
    public Set<String> getHosts() {
        try (InputStream input = IOUtils.getStream(this.catalogServiceURI.resolve(HOSTS_ENDPOINT))) {
            return this.objectMapper.readValue(input, Set.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public void write(final OutputStream outputStream) throws IOException {
        try (InputStream input = IOUtils.getStream(this.catalogServiceURI.resolve(WRITE_ENDPOINT))) {
            byte[] buffer = new byte[BYTE_BUFFER_SIZE];
            int i = 0;
            while (true) {
                i = input.read(buffer);
                if (i == -1) {
                    break;
                }
                outputStream.write(buffer, 0, i);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
