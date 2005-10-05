package edu.stanford.laneweb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.marc4j.ErrorHandler;
import org.marc4j.MarcHandler;

import edu.stanford.lane.catalog.MarcReader;


public class MarcReaderImpl
    extends AbstractLogEnabled
    implements MarcReader, Initializable, Recyclable {

    org.marc4j.MarcReader myReader;

    public void initialize() {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("initialize()");
        }
        myReader = new org.marc4j.MarcReader();
    }

    public void parse(String aFile) throws IOException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("parse(String)");
        }
        myReader.parse(aFile);
    }

    public void parse(InputStreamReader aReader) throws IOException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("parse(InputStreamReader)");
        }
        myReader.parse(aReader);
    }

    public void parse(InputStream aStream) throws IOException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("parse(InputStream)");
        }
        myReader.parse(aStream);
    }

    public void parse(Reader aReader) throws IOException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("parse(Reader)");
        }
        myReader.parse(aReader);
    }

    public void setErrorHandler(ErrorHandler aErrorHandler) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("setErrorHandler()");
        }
        myReader.setErrorHandler(aErrorHandler);
    }

    public void setMarcHandler(MarcHandler aMarcHandler) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("setMarcHandler()");
        }
        myReader.setMarcHandler(aMarcHandler);
    }

    public void recycle() {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("recycle()");
        }
        myReader.setMarcHandler(null);
        myReader.setErrorHandler(null);
    }

}
