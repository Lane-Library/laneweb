package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public abstract class AbstractEresourcesGenerator extends ServiceableGenerator {

    private static final String KEYWORDS = "keywords";

    private static final String QUERY = "q";

    private static final String TYPE = "t";

    private static final String SUBSET = "s";

    private static final String ALPHA = "a";

    private static final String MESH = "m";

    private static final Attributes EMPTY_ATTS = new AttributesImpl();

    private DataSourceComponent dataSource;

    protected String query;

    protected String subset;

    protected String type;

    protected String alpha;

    protected String mesh;

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.query = request.getParameter(QUERY);
        if (null == this.query) {
            this.query = request.getParameter(KEYWORDS);
        }
        if (null != this.query) {
            if (this.query.length() == 0) {
                this.query = null;
            }
        }
        this.type = request.getParameter(TYPE);
        if (null != this.type) {
            if (this.type.length() == 0) {
                this.type = null;
            }
        }
        this.subset = request.getParameter(SUBSET);
        if (null != this.subset) {
            if (this.subset.length() == 0) {
                this.subset = null;
            }
        }
        this.alpha = request.getParameter(ALPHA);
        if (this.alpha != null) {
            if (this.alpha.length() == 0) {
                this.alpha = null;
            }
        }
        this.mesh = request.getParameter(MESH);
        if (this.mesh != null) {
            if (this.mesh.length() == 0) {
                this.mesh = null;
            }
        }
    }

    public void generate() throws SAXException, ProcessingException {
        List<Eresource> eresources = getEresourceList();
        String title = "edu.stanford.irt.eresources.Eresource";
        EresourceSAXTranslator translator = new EresourceSAXTranslator();
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html",
                "html", EMPTY_ATTS);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "head",
                "head", EMPTY_ATTS);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "title",
                "title", EMPTY_ATTS);
        this.xmlConsumer.characters(title.toCharArray(), 0, title.length());
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "title",
                "title");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "head",
                "head");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "body",
                "body", EMPTY_ATTS);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "dl",
                "dl", EMPTY_ATTS);
        for (Eresource er : eresources) {
            translator.translate(this.xmlConsumer, er);
        }
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dl", "dl");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "body",
                "body");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "html",
                "html");
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
    }

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        ServiceSelector selector = (ServiceSelector) manager
                .lookup(DataSourceComponent.ROLE + "Selector");
        this.dataSource = (DataSourceComponent) selector.select("eresources");
        this.manager.release(selector);
    }

    @Override
    public void dispose() {
        this.manager.release(this.dataSource);
        super.dispose();
    }

    protected List<Eresource> getEresourceList() throws ProcessingException {
        List<Eresource> eresources = new LinkedList<Eresource>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            try {
                stmt = getStatement(conn);
            } catch (IllegalArgumentException e) {
                // if can't create a SQL query, return the empty list
                return eresources;
            }
            rs = stmt.executeQuery();
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
        return eresources;
    }

    protected abstract PreparedStatement getStatement(Connection conn)
            throws SQLException;

}
