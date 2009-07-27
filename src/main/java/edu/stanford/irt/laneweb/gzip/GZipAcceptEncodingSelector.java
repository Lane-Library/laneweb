package edu.stanford.irt.laneweb.gzip;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.processing.ProcessInfoProvider;
import org.apache.cocoon.selection.Selector;


public class GZipAcceptEncodingSelector implements Selector {

    private ProcessInfoProvider pip;
    
    public void setProcessInfoProvider(ProcessInfoProvider pip) {
        this.pip = pip;
    }

    @SuppressWarnings("unchecked")
    public boolean select(String expression, Map objectModel, Parameters parameters) {
        String header = pip.getRequest().getHeader("Accept-Encoding");
        return header != null ? header.contains("gzip") : false;
    }
}
