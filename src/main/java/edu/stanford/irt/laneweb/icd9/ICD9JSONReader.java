package edu.stanford.irt.laneweb.icd9;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;

import edu.stanford.irt.lane.icd9.ICD9Translator;

/**
 * Provides a JSON representation of translated ICD9 codes passed as icd9
 * parameter. If successful like this: {"code":"804","longName":"some text"}. If
 * not then like this: {"code":"foo","error":"foo is not an icd9 code"}. Note:
 * this Reader is not thread safe.
 * 
 * @author ceyates
 */
public class ICD9JSONReader implements Reader {

    private String icd9;

    private OutputStream outputStream;

    private ICD9Translator translator;

    public void generate() throws IOException {
        StringBuffer sb = new StringBuffer("{\"code\":\"").append(this.icd9.replaceAll("\\\"", "\\\\\"")).append("\",");
        try {
            String result = this.translator.getLongName(this.icd9);
            sb.append("\"longName\":\"").append(result);
        } catch (IllegalArgumentException e) {
            sb.append("\"error\":\"").append(e.getMessage().replaceAll("\\\"", "\\\\\""));
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
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.icd9 = par.getParameter("icd9", null);
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
