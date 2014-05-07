package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class HttpResource extends UrlResource {

    private String authorization;

    private URL url;

    public HttpResource(final URL url, final String userName, final String password) throws IOException {
        this(url, Base64.encodeBase64String((userName.concat(":").concat(password)).getBytes(Charset.forName("UTF-8"))));
    }

    private HttpResource(final URL url) throws MalformedURLException {
        super(url);
        this.url = url;
    }

    private HttpResource(final URL url, final String authorization) throws MalformedURLException {
        this(url);
        this.authorization = authorization;
    }

    @Override
    public Resource createRelative(final String path) throws MalformedURLException {
        String relativePath = path.startsWith("/") ? path.substring(1) : path;
        return new HttpResource(new URL(this.url, relativePath), this.authorization);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        con.setRequestProperty("Authorization", "Basic " + this.authorization);
        con.setUseCaches(false);
        return con.getInputStream();
    }
}
