package edu.stanford.irt.laneweb.icd9;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;
import org.xml.sax.SAXException;

import edu.stanford.irt.lane.icd9.ICD9Translator;

/**
 * Provides a JSON representation of translated ICD9 codes passed as icd9
 * parameter. If successful like this: {"icd9":"some text"}. If not then like
 * this: {"error":"error text"}. Note: this Reader is not thread safe.
 * 
 * @author ceyates
 */
public class ICD9JSONReader implements Reader {

    private String icd9;

    private OutputStream outputStream;

    private ICD9Translator translator;

    public void generate() throws IOException {
        StringBuffer sb = new StringBuffer("{");
        try {
            String result = this.translator.translate(this.icd9);
            sb.append("\"icd9\":\"").append(result);
        } catch (IllegalArgumentException e) {
            sb.append("\"error\":\"").append(e.getMessage());
        }
        sb.append("\"}");
        this.outputStream.write(sb.toString().getBytes());
    }

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return "text/plain";
    }

    public void setICD9Translator(final ICD9Translator translator) {
        this.translator = translator;
    }

    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) throws ProcessingException, SAXException,
            IOException {
        this.icd9 = par.getParameter("icd9", null);
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
