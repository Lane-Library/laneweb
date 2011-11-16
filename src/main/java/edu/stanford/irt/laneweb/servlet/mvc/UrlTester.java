package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
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
        GetMethod get = new GetMethod(url);
        this.httpClient.executeMethod(get);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(get.getResponseBody());
        byte[] headers = getHeaderString(get);
        baos.write(headers);
        response.setHeader("Content-Type", "text/plain");
        response.getOutputStream().write(baos.toByteArray());
    }

    @Autowired
    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.httpClient = msms.getHttpClient();
    }

    private byte[] getHeaderString(final GetMethod get) {
        StringBuffer result = new StringBuffer("\n\n\n<!--\n\nRequest Headers:\n\n");
        result.append(getHeaderString(get.getRequestHeaders()));
        result.append("\n\n\nResponse Headers:\n\n");
        result.append(getHeaderString(get.getResponseHeaders()));
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
}
