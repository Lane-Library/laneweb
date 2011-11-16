package edu.stanford.irt.laneweb.servlet.mvc;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.search.MetaSearchManagerSource;

@Controller
public class UrlTester {

    private HttpClient httpClient;

    @RequestMapping(value = "/apps/url-tester")
    public void testUrl(@RequestParam final String url, HttpServletResponse response) throws IOException {
        HttpGet get = new HttpGet(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        HttpResponse res = null; 
        try {
        	res	= this.httpClient.execute(get);
        	baos.write(EntityUtils.toByteArray(res.getEntity()));
		} catch (Exception e) {
			get.abort();
		}byte[] headers = getHeaderString(get, res);
        baos.write(headers);
        response.setHeader("Content-Type", "text/plain");
        response.getOutputStream().write(baos.toByteArray());
    }

    
    private byte[] getHeaderString(final HttpGet get, final HttpResponse rep) {
        StringBuffer result = new StringBuffer("\n\n\n<!--\n\nRequest Headers:\n\n");
        result.append(getHeaderString(get.getAllHeaders()));
        result.append("\n\n\nResponse Headers:\n\n");
        result.append(getHeaderString(get.getAllHeaders()));
        result.append("\n-->");
        return result.toString().getBytes();
    }

    private String getHeaderString(final Header[] headers) {
        StringBuffer result = new StringBuffer();
        for (Header header : headers) {
            result.append(header.getName());
            result.append(" ==> ");
            result.append(header.getValue());
            result.append("\n");
        }
        return result.toString();
    }
    
    @Autowired
    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.httpClient = msms.getHttpClient();
    }
}
