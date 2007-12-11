package edu.stanford.irt.laneweb.eresources;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public abstract class AbstractEresources  extends ServiceableGenerator {

	private static final Attributes EMPTY_ATTS = new AttributesImpl();
	
	String sql;

    DataSourceComponent dataSource;
	
	
	public void generate()throws SAXException, ProcessingException
	{
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
	                    eresources.add(eresource);
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
	            String title = "edu.stanford.irt.eresources.Eresource";
	            EresourceSAXTranslator translator = new EresourceSAXTranslator();
	            this.xmlConsumer.startDocument();
	            this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
	            this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html", "html", EMPTY_ATTS);
	            this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "head", "head", EMPTY_ATTS);
	            this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "title", "title", EMPTY_ATTS);
	            this.xmlConsumer.characters(title.toCharArray(), 0, title.length());
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
	        } finally {
	            try {
	                if (null != conn) {
	                    conn.close();
	                }
	                if (null != stmt) {
	                    stmt.close();
	                }
	                if (null != rs) {
	                    rs.close();
	                }
	            } catch (SQLException e) {
	                throw new ProcessingException(e);
	            }
	        }
	    }
	
	

    @Override
    public void recycle() {
        this.sql = null;
    }

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        ServiceSelector selector = (ServiceSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
        this.dataSource = (DataSourceComponent) selector.select("eresources");
        this.manager.release(selector);
    }

    @Override
    public void dispose() {
        this.manager.release(this.dataSource);
        super.dispose();
    }


	
}
