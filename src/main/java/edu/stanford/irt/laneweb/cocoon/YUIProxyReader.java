package edu.stanford.irt.laneweb.cocoon;

import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

public class YUIProxyReader extends NoCacheBigReader {

    /*
     * determines mimetype from end of parameters 
     */
    @Override
    public String getMimeType() {
        String uri = this.source.getURI();
        int index = uri.lastIndexOf('.');
        if (index > 0) {
            String suffix = uri.substring(index);
            if (".css".equals(suffix)) {
                return "text/css";
            } else if (".js".equals(suffix)) {
                return "application/javascript";
            }
        }
        return null;
    }

    /*
     * always valid
     */
    @Override
    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }
}
