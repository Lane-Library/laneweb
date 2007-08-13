/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;


public class EresourcesSearchGenerator extends ServiceableGenerator {
	
	public static void main(String[] args) {
		System.out.println(SEARCH_SQL_1.replaceAll("XX", "(({cardiology}))") + SEARCH_SQL_2);
	}

	private static final String SEARCH_SQL_1 =
		"WITH FOUND AS (\n" +
		"SELECT TITLE, ERESOURCE2.ERESOURCE_ID, SCORE(1) * 3 AS SCORE_TEXT,\n" +
		"CONTAINS(TITLE,'XX') * 3 AS SCORE_TITLE\n" +
		"FROM ERESOURCE2\n" +
		"WHERE CONTAINS(TEXT,'XX',1) > 0\n" +
		"AND CORE = 'Y'\n" +
		"UNION\n" +
		"SELECT PREFERRED_TITLE AS TITLE, ERESOURCE2.ERESOURCE_ID, SCORE(1) * 3 AS SCORE_TEXT,\n"+
		"CONTAINS(PREFERRED_TITLE,'XX') * 3 AS SCORE_TITLE\n" +
		"FROM ERESOURCE2\n" +
		"WHERE CONTAINS(TEXT,'XX',1) > 0\n" +
		"AND CORE = 'Y'\n" +
		"AND PREFERRED_TITLE IS NOT NULL\n" +
		"UNION\n" +
		"SELECT TITLE, ERESOURCE2.ERESOURCE_ID, SCORE(1) AS SCORE_TEXT,\n"+
		"CONTAINS(TITLE,'XX') AS SCORE_TITLE\n" +
		"FROM ERESOURCE2\n" +
		"WHERE CONTAINS(TEXT,'XX',1) > 0\n" +
		"AND CORE IS NULL\n" +
		"UNION\n" +
		"SELECT PREFERRED_TITLE AS TITLE, ERESOURCE2.ERESOURCE_ID,\n"+
		"SCORE(1) AS SCORE_TEXT, CONTAINS(PREFERRED_TITLE,'XX') AS SCORE_TITLE\n" +
		"FROM ERESOURCE2\n" +
		"WHERE CONTAINS(TEXT,'XX',1) > 0\n" +
		"AND CORE IS NULL\n" +
		"AND PREFERRED_TITLE IS NOT NULL\n" +
		")\n" +
		"SELECT FOUND.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER,\n"+
		"HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"+
		"SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n" +
		"FROM FOUND, VERSION2, LINK2, TYPE2, SUBSET2\n" +
		"WHERE FOUND.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n" +
		"AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n" +
		"AND FOUND.ERESOURCE_ID = TYPE2.ERESOURCE_ID(+)\n" +
		"AND VERSION2.VERSION_ID = SUBSET2.VERSION_ID(+)\n";
	
	public static final String SEARCH_SQL_2 =
		"ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";
	
    private static final String KEYWORDS = "keywords";
    private static final String QUERY = "q";
    private static final String TYPE = "t";
    private static final String SUBSET = "s";
    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private String sql;
    private String requestQuery;
    private QueryTranslator queryTranslator = new QueryTranslator();

	private DataSourceComponent dataSource;

    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters par) throws ProcessingException, SAXException,
            IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        if (query != null) {
            if (query.length() ==0) {
                query = null;
            }
        }
        if (query == null) {
			query = request.getParameter(KEYWORDS);
			if (query != null) {
				if (query.length() == 0) {
					query = null;
				}
			}
		}
        requestQuery = query;
        String type = request.getParameter(TYPE);
        if (type != null) {
            if (type.length() == 0) {
                type = null;
            } else {
                type = type.toLowerCase().replaceAll("'", "''");
            }
        }
        String subset = request.getParameter(SUBSET);
        if (subset != null) {
            if (subset.length() == 0) {
                subset = null;
            } else {
                subset = subset.toLowerCase();
            }
        }
        if (query != null) {
        	query = query.replaceAll("'","''");
        	String translatedQuery = this.queryTranslator.translate(query);
        	StringBuffer sb = new StringBuffer(SEARCH_SQL_1.replaceAll("XX", translatedQuery));
        	if (null != subset) {
        		sb.append("AND SUBSET ='").append(subset).append("'\n");
        	}
        	if (null != type) {
        		sb.append("AND TYPE = '").append(type).append("'\n");
        	}
        	sb.append(SEARCH_SQL_2);
        	this.sql = sb.toString();
        } else {
            this.sql = "";
        }
    }

    public void generate() throws SAXException, ProcessingException {
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
	    	stmt = conn.createStatement();
	    	rs = stmt.executeQuery(this.sql);
	    	List<Eresource> eresources = new LinkedList<Eresource>();
	    	Eresource eresource = null;
	    	Version version = null;
	    	Link link;
	    	int currentEresourceId = -1;
	    	int currentVersionId = -1;
	    	int currentLinkId = -1;
	    	while (rs.next()) {
	    		int rowEresourceId = rs.getInt("ERESOURCE_ID");
	    		if (rowEresourceId != currentEresourceId) {
	    			eresource = new Eresource();
	    			eresource.setId(rowEresourceId);
	    			String title = rs.getString("TITLE");
	    			eresource.setTitle(title);
	    			if (requestQuery.equalsIgnoreCase(title)) {
	    				eresources.add(0, eresource);
	    			} else {
	    				eresources.add(eresource);
	    			}
	    			currentEresourceId = rowEresourceId;
	    		}
	    		int rowVersionId = rs.getInt("VERSION_ID");
	    		if (rowVersionId != currentVersionId) {
	    			version = new Version();
	    			eresource.addVersion(version);
	    			version.setPublisher(rs.getString("PUBLISHER"));
	    			version.setSummaryHoldings(rs.getString("HOLDINGS"));
	    			version.setDates(rs.getString("DATES"));
	    			version.setDescription(rs.getString("DESCRIPTION"));
	    			if ("F".equals(rs.getString("PROXY"))) {
	    				version.setProxy(false);
	    			}
	    			currentVersionId = rowVersionId;
	    		}
	    		int rowLinkId = rs.getInt("LINK_ID");
	    		if (rowLinkId != currentLinkId) {
	    			link = new Link();
	    			version.addLink(link);
	    			link.setUrl(rs.getString("URL"));
	    			link.setLabel(rs.getString("LABEL"));
	    			link.setInstruction(rs.getString("INSTRUCTION"));
	    			currentLinkId = rowLinkId;
	    		}
	    	}
	    	EresourceSAXTranslator translator = new EresourceSAXTranslator();
	    	this.xmlConsumer.startDocument();
	    	this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
	    	this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html", "html", EMPTY_ATTS);
	    	this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "head", "head", EMPTY_ATTS);
	    	this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "title", "title", EMPTY_ATTS);
	    	this.xmlConsumer.characters(new String("laneconnex search results").toCharArray(), 0, 25);
	    	this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "title", "title");
	    	this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "head", "head");
	    	this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "body", "body", EMPTY_ATTS);
	    	this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "dl", "dl", EMPTY_ATTS);
	    	for (Eresource er : eresources) {
	    		translator.translate(this.xmlConsumer, er);
	    	}
	    	this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dl", "dl");
	    	this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "body", "body");
	    	this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "html", "html");
	    	this.xmlConsumer.endPrefixMapping("");
	    	this.xmlConsumer.endDocument();
		} catch (SQLException e) {
			throw new ProcessingException(e);
		}
		finally {
			try {
				if (null != conn) 
				conn.close();
				if (null != stmt)
				stmt.close();
				if (null != rs)
				rs.close();
			} catch (SQLException e) {
				throw new ProcessingException(e);
			}
		}
    }
    
    public void recycle() {
    	this.sql = null;
    	this.requestQuery = null;
    }
    
    public void service(ServiceManager manager) throws ServiceException {
    	super.service(manager);
    	ServiceSelector selector = (ServiceSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    	this.dataSource = (DataSourceComponent) selector.select("eresources");
    	this.manager.release(selector);
    }
    
    public void dispose() {
    	this.manager.release(this.dataSource);
    	super.dispose();
    }
    
}
