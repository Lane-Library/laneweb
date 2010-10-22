package edu.stanford.irt.laneweb.search;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractReader;
import edu.stanford.irt.laneweb.model.Model;

public class UrlTester extends AbstractReader {

    private HttpClient httpClient;

    private String url;

    public void generate() throws IOException, SAXException {
        GetMethod get = new GetMethod(this.url);
        this.httpClient.executeMethod(get);
        this.outputStream.write(get.getResponseBody());
        byte[] headers = getHeaderString(get);
        this.outputStream.write(headers);
        this.outputStream.flush();
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

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

    @Override
    protected void initialize() {
        this.url = getString(this.model, Model.URL);
        if (this.url == null) {
            throw new IllegalStateException(Model.URL + " is null");
        }
        if (!this.url.startsWith("http")) {
            this.url = "http://".concat(this.url);
        }
    }
}
