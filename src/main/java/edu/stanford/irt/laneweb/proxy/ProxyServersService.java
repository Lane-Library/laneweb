package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public interface ProxyServersService {

    Set<String> getHosts();

    void write(OutputStream outputStream) throws IOException;
}
