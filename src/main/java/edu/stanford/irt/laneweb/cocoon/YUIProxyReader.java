package edu.stanford.irt.laneweb.cocoon;

import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;


public class YUIProxyReader extends NoCacheBigReader {
    
    private String mimeType;

    @Override
    public String getMimeType() {
        if (this.mimeType == null) {
            String uri = this.source.getURI();
            int index = uri.lastIndexOf('.');
            if (index > 0) {
                String suffix = uri.substring(index);
                if (".css".equals(suffix)) {
                    this.mimeType = "text/css";
                } else if (".js".equals(suffix)) {
                    this.mimeType = "application/javascript";
                }
            }
        }
        return this.mimeType;
    }

    @Override
    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }
}
