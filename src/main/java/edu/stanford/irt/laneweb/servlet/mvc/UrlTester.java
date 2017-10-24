package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

@Controller
public class UrlTester {

    private MetaSearchService metaSearchService;

    @Autowired
    public UrlTester(final MetaSearchService metaSearchService) {
        this.metaSearchService = metaSearchService;
    }

    @RequestMapping(value = "/apps/url-tester")
    public void testUrl(@RequestParam final String url, final HttpServletResponse response) {
        HttpGet httpGet = new HttpGet(url);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            HttpResponse httpResponse = this.metaSearchService.execute(httpGet);
            baos.write(EntityUtils.toByteArray(httpResponse.getEntity()));
            byte[] headers = getHeaderString(httpGet, httpResponse);
            baos.write(headers);
            response.setHeader("Content-Type", "text/plain");
            response.getOutputStream().write(baos.toByteArray());
        } catch (IOException e) {
            throw new LanewebException(e);
        } finally {
            httpGet.abort();
        }
    }

    private String getHeaderString(final Header[] headers) {
        StringBuilder result = new StringBuilder();
        for (Header header : headers) {
            result.append(header.getName());
            result.append(" ==> ");
            result.append(header.getValue());
            result.append('\n');
        }
        return result.toString();
    }

    private byte[] getHeaderString(final HttpGet httpGet, final HttpResponse httpResponse) {
        StringBuilder result = new StringBuilder("\n\n\n<!--\n\nRequest Headers:\n\n");
        result.append(getHeaderString(httpGet.getAllHeaders()));
        result.append("\n\n\nResponse Headers:\n\n");
        result.append(getHeaderString(httpResponse.getAllHeaders()));
        result.append("\n-->");
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }
}
