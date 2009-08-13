package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import sun.misc.BASE64Encoder;

public class HttpResource extends UrlResource {

    private String authorization;

    private URL context;

    public HttpResource(final URL url) throws MalformedURLException {
        super(url);
        this.context = url;
    }

    public HttpResource(final URL url, final String authorization) throws MalformedURLException {
        this(url);
        this.authorization = authorization;
    }

    public HttpResource(final URL url, final String userName, final String password) throws MalformedURLException {
        this(url);
        this.authorization = new BASE64Encoder().encode((userName.concat(":").concat(password)).getBytes());
    }

    @Override
    public Resource createRelative(final String path) throws MalformedURLException {
        String relativePath = path.startsWith("/") ? path.substring(1) : path;
        return new HttpResource(new URL(this.context, relativePath), this.authorization);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = super.getURL().openConnection();
        con.setRequestProperty("Authorization", "Basic " + this.authorization);
        con.setUseCaches(false);
        return con.getInputStream();
    }
}
