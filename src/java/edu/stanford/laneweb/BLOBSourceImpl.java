package edu.stanford.laneweb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;

import edu.stanford.lane.catalog.BLOBSource;

public class BLOBSourceImpl extends AbstractLogEnabled implements Serviceable, BLOBSource, ThreadSafe, Parameterizable {

    private static final String QUERY_PREFIX = "{? = call ";

    private static final String BIB_QUERY =
        ".getbibblob(?) }";

    private static final String MFHD_QUERY =
        ".getmfhdblob(?) }";

    private static final String AUTH_QUERY =
        ".getauthblob(?) }";
    
    private static HashMap QUERYS = new HashMap();
    
    static {
        QUERYS.put( BLOBSource.BIB, BIB_QUERY );
        QUERYS.put( BLOBSource.MFHD, MFHD_QUERY );
        QUERYS.put( BLOBSource.AUTH, AUTH_QUERY );
    }
    
    private ServiceManager myManager;
    
    private String myDBName;
    
    public void parameterize( Parameters params ) throws ParameterException {
        myDBName = params.getParameter("db-name", "voyager");
    }
    
    public byte[] getBlobs(String db, String type, Collection idList) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Iterator i = idList.iterator(); i.hasNext();) {
            try {
                baos.write(getBlob(db, type, i.next().toString()));
            }
            catch (IOException e) {
                getLogger().error(e.getMessage(), e);
            }
        }
        return baos.toByteArray();
    }
    
    
    public InputStream getBlobInputStream(final String db, final String type, final Collection ids) {
        final PipedInputStream input = new PipedInputStream();
        try {
            final PipedOutputStream output = new PipedOutputStream(input);
            Thread thread = new Thread() {
                public void run() {
                    try {
                        for (Iterator i = ids.iterator(); i.hasNext();) {
                            output.write(getBlob(db, type, i.next().toString()));
                        }
                    }
                    catch (Exception e) {
                        getLogger().error(e.getMessage(), e);
                    }
                    finally {
                        try {
                            output.close();
                        }
                        catch (Exception e) {
                            getLogger().error(e.getMessage(), e);
                        }
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
        catch (Exception e) {
            getLogger().error(e.toString(), e);
        }
        return input;
    }
    
    
    public byte[] getBlob(String db, String type, String id) {
        if ( getLogger().isDebugEnabled() ) {
            getLogger().debug("getting " + type + " blob for id " + id + " from " + db);
            getLogger().debug(QUERYS.toString());
            getLogger().debug(Boolean.toString(QUERYS.keySet().contains(type)));
        }
        String query = new StringBuffer(QUERY_PREFIX).append(db).append((String) QUERYS.get(type)).toString();
        byte[] result = new byte[0];
        Connection conn = null;
        DataSourceComponent source = null;
        ServiceSelector selector = null;
        CallableStatement stmt = null;
        try {
            selector = (ServiceSelector) myManager.lookup(DataSourceComponent.ROLE + "Selector");
            source = (DataSourceComponent) selector.select(myDBName);
            conn = source.getConnection();
            stmt = conn.prepareCall(query);
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, id);
            stmt.execute();
            if (stmt.getObject(1) != null) {
                result = stmt.getBytes(1);
            }
        }
        catch (Throwable t) {
            getLogger().error(query, t);
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (SQLException e) {
                    if (getLogger().isErrorEnabled()) {
                        getLogger().error(e.getMessage(), e);
                    }
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    if (getLogger().isErrorEnabled()) {
                        getLogger().error(e.getMessage(), e);
                    }
                }
            }
            if ( selector != null ) {
                selector.release( source );
                myManager.release(selector);
            }
        }
        return result;
    }

	/* (non-Javadoc)
	 * @see edu.stanford.lane.component.BLOBSource#getBlobInputStream(java.lang.String, java.lang.String)
	 */
	public InputStream getBlobInputStream(String db, String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.component.Composable#compose(org.apache.avalon.framework.component.ComponentManager)
	 */
	public void service(ServiceManager aManager) throws ServiceException {
		myManager = aManager;
	}
    
}
