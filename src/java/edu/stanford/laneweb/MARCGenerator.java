package edu.stanford.laneweb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.xml.sax.SAXException;

import edu.stanford.lane.catalog.BLOBSource;
import edu.stanford.lane.catalog.MarcReader;
import edu.stanford.lane.marc4j.MARCtoSAXHandler;

public class MARCGenerator extends ServiceableGenerator implements Initializable {
    
    
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String DB = "db";
    
    private MARCtoSAXHandler myHandler;
    
    private String id = null;
    private String db = null;
    private String type = null;
    
    public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
    throws ProcessingException, SAXException, IOException {
        if ( getLogger().isDebugEnabled() ) {
            getLogger().debug( "setup()" );
        }
        super.setup(resolver, objectModel, src, par);
        id = par.getParameter( ID, "0" );
        type = par.getParameter( TYPE, "bib" );
        db = par.getParameter(DB, "lmldb");
    }
    
    public void generate() throws IOException, ProcessingException {
        if ( getLogger().isDebugEnabled() ) {
            getLogger().debug( "generate()" );
        }
        BLOBSource source = null;
        MarcReader reader = null;
        try {
			source = (BLOBSource) manager.lookup(BLOBSource.ROLE);
			reader = (MarcReader) manager.lookup(MarcReader.ROLE);
			reader.setMarcHandler(myHandler);
			myHandler.setContentHandler(this.contentHandler);
            ArrayList idList = null;
            if ( id.indexOf(",") > -1 ) {
                idList = new ArrayList();
                for ( StringTokenizer st = new StringTokenizer(id, ","); st.hasMoreTokens();) {
                    idList.add( st.nextToken() );
                }
            }
            
            if ( idList == null ) {
            	reader.parse(new ByteArrayInputStream(source.getBlob(db, type, id)));
            }
            else {
            	reader.parse(new ByteArrayInputStream(source.getBlobs(db, type, idList)));
            }
            
        }
        catch ( ServiceException e ) {
            if ( getLogger().isErrorEnabled() ) {
                getLogger().error( e.getMessage(), e );
            }
            throw new ProcessingException( e );
        }
        finally {
            this.manager.release( source );
            this.manager.release(reader);
        }
    }

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.activity.Initializable#initialize()
	 */
	public void initialize() throws Exception {
		myHandler = new MARCtoSAXHandler();
		myHandler.enableLogging(getLogger().getChildLogger("marc-handler"));
	}
    
}
