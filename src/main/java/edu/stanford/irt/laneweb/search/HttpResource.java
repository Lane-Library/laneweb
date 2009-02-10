package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import sun.misc.BASE64Encoder;

public class HttpResource extends UrlResource{

    private static String authorization;
    
    private URL context;

    public HttpResource(URL url) throws MalformedURLException {
	super(url);
	this.context =  url;
    }

    
    public HttpResource(URL url, String userName, String password) throws MalformedURLException {
	this(url);
	authorization = new  BASE64Encoder().encode((userName.concat(":").concat(password)).getBytes());
    }
    
    
    public InputStream getInputStream() throws IOException {
	URLConnection con = super.getURL().openConnection();
	con.setRequestProperty("Authorization", "Basic "+authorization);
	con.setUseCaches(false);
	return con.getInputStream();
	
    }

    
    public Resource createRelative(String relativePath) throws MalformedURLException {
	if (relativePath.startsWith("/")) {
		relativePath = relativePath.substring(1);
	}
	return new HttpResource(new URL(context , relativePath));
    }
}
