package edu.stanford.irt.laneweb.gzip;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.processing.ProcessInfoProvider;
import org.apache.cocoon.selection.Selector;

public class GZipAcceptEncodingSelector implements Selector {

    private ProcessInfoProvider pip;

    @SuppressWarnings("unchecked")
    public boolean select(final String expression, final Map objectModel, final Parameters parameters) {
        String header = this.pip.getRequest().getHeader("Accept-Encoding");
        return header != null ? header.contains("gzip") : false;
    }

    public void setProcessInfoProvider(final ProcessInfoProvider pip) {
        this.pip = pip;
    }
}
