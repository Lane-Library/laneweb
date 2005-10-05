package edu.stanford.lane.catalog;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.marc4j.MarcHandler;
import org.marc4j.ErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public interface MarcReader extends Recyclable {
    
    public static final String ROLE = MarcReader.class.getName();
    
    public void setMarcHandler( MarcHandler aMarcHandler );
    
    public void setErrorHandler( ErrorHandler aErrorHandler );
    
    public void parse( String aFile ) throws IOException;
    
    public void parse( InputStream aStream ) throws IOException;
    
    public void parse( InputStreamReader aReader ) throws IOException;
    
    public void parse( Reader aReader ) throws IOException;
    
}
