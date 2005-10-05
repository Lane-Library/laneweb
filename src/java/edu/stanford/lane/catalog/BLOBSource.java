package edu.stanford.lane.catalog;

import java.util.Collection;
import java.io.InputStream;

public interface BLOBSource {
    
    public static final String ROLE = BLOBSource.class.getName();
    
    public static final String BIB = "bib";
    
    public static final String MFHD = "mfhd";
    
    public static final String AUTH = "auth";
    
    public byte[] getBlob( String db, String type, String id );
    
    public byte[] getBlobs( String db, String type, Collection ids );
    
    public InputStream getBlobInputStream( String db, String type, Collection ids );
    
    public InputStream getBlobInputStream(String db, String type, String query);
    
}
