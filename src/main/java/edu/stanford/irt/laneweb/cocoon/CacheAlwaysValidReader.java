package edu.stanford.irt.laneweb.cocoon;

import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;


public class CacheAlwaysValidReader extends NoCacheBigReader {

    @Override
    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }
}
