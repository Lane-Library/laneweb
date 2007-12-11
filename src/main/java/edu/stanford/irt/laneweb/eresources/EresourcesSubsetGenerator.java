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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;

public class EresourcesSubsetGenerator extends EresourcesBrowseGenerator {

    private static final String BROWSE_SQL_1 =
            "SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TITLE, PUBLISHER,\n"
                    + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n" + "FROM ERESOURCE2, VERSION2, LINK2, SUBSET2";

    private static final String BROWSE_SQL_3 =
            "\nWHERE ERESOURCE2.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n" + "AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n";

    private static final String BROWSE_SQL_4 = "AND VERSION2.VERSION_ID = SUBSET2.VERSION_ID\n" + "AND SUBSET2.SUBSET = '";

    private static final String BROWSE_SQL_8 =
            "UNION SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER,\n"
                    + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
                    + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
                    + "FROM ERESOURCE2, VERSION2, LINK2, SUBSET2";

    private static final String BROWSE_SQL_11 = "AND PREFERRED_TITLE IS NOT NULL\n";

    private static final String BROWSE_SQL_12 = "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SUBSET = "s";

    private static final Attributes EMPTY_ATTS = new AttributesImpl();

    private String sql;

    private DataSourceComponent dataSource;

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String subset = request.getParameter(SUBSET);
        if (subset != null) {
            if (subset.length() == 0) {
                subset = null;
            } else {
                subset = subset.toLowerCase().replaceAll("'", "''");
            }
        }
        if (null == subset) {
            throw new ProcessingException("no subset parameter provided");
        }
        this.sql = createSQL(subset);
    }

    @Override
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
            EresourceSAXTranslator translator = new EresourceSAXTranslator();
            this.xmlConsumer.startDocument();
            this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
            this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html", "html", EMPTY_ATTS);
            this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "head", "head", EMPTY_ATTS);
            this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "title", "title", EMPTY_ATTS);
            this.xmlConsumer.characters(new String("laneconnex browse").toCharArray(), 0, 17);
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

    private String createSQL(final String subset) {
        StringBuffer sb = new StringBuffer(BROWSE_SQL_1);
        sb.append(BROWSE_SQL_3);
        if (null != subset) {
            sb.append(BROWSE_SQL_4).append(subset).append("'\n");
        }
        sb.append(BROWSE_SQL_8);
        sb.append(BROWSE_SQL_3);
        if (null != subset) {
            sb.append(BROWSE_SQL_4).append(subset).append("'\n");
        }
        sb.append(BROWSE_SQL_11);
        sb.append(BROWSE_SQL_12);
        return sb.toString();
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
