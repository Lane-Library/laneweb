package edu.stanford.irt.laneweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.ResourceReader;
import org.xml.sax.SAXException;

public class TxtResourceReader extends ResourceReader {

    String configuredPath = null;

    String path = null;

    String valueToSubstitute = null;

    @Override
    public void configure(final Configuration configuration)
            throws ConfigurationException {
        super.configure(configuration);
        this.configuredPath = configuration.getChild("path").getValue();
        this.valueToSubstitute = configuration.getChild("valueToSubstitute")
                .getValue();
    }

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        this.path = par.getParameter("path", this.configuredPath);
    }

    @Override
    public void recycle() {
        super.recycle();
        this.path = null;
    }

    @Override
    public Serializable getKey() {
        return this.inputSource.getURI() + ";path=" + this.path;
    }

    @Override
    protected void processStream(final InputStream inputStream)
            throws IOException, ProcessingException {
        byte[] buffer = new byte[this.bufferSize];
        String ranges = this.request.getHeader("Range");
        long contentLength = this.inputSource.getContentLength();

        if (this.byteRanges && (ranges != null)) {
            throw new ProcessingException("Reader only for Text file");
        }
        if (contentLength != -1) {
            this.response.setHeader("Content-Length", Long
                    .toString(contentLength));
        }
        StringBuffer page = new StringBuffer();
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                inputStream));
        String line = null;
        while ((line = bf.readLine()) != null) {
            page.append(line);
            page.append("\n");
        }
        String pageToSend = page.toString().replaceAll(this.valueToSubstitute,
                this.path);
        buffer = pageToSend.getBytes();
        this.out.write(buffer, 0, buffer.length);
        this.out.flush();
    }
}
