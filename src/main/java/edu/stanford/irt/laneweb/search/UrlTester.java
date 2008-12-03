package edu.stanford.irt.laneweb.search;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.SAXException;

public class UrlTester implements Reader {

    private HttpClient httpClient;

    private OutputStream outputStream;

    private String url;

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.httpClient = msms.getHttpClient();
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        this.url = par.getParameter("url", null);
        if (!this.url.startsWith("http")) {
            this.url = "http://".concat(this.url);
        }
    }

    public void generate() throws IOException, SAXException, ProcessingException {
        GetMethod get = new GetMethod(this.url);
        this.httpClient.executeMethod(get);
        this.outputStream.write(get.getResponseBody());
        byte[] headers = getHeaderString(get);
        this.outputStream.write(headers);
        this.outputStream.flush();
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

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return null;
    }

    public void setOutputStream(final OutputStream out) {
        if ((out instanceof BufferedOutputStream) || (out instanceof org.apache.cocoon.util.BufferedOutputStream)) {

            this.outputStream = out;
        } else {
            this.outputStream = new BufferedOutputStream(out, 1536);
        }
    }

    public boolean shouldSetContentLength() {
        return false;
    }

    public SourceValidity getValidity() {
        return null;
    }
}
