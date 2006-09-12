package edu.stanford.laneweb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.ExtendedComponentSelector;
import org.apache.cocoon.transformation.AbstractDOMTransformer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;
import org.apache.excalibur.xml.xpath.XPathProcessor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ObjectIDTransformer extends AbstractDOMTransformer 
	implements Parameterizable, Initializable, CacheableProcessingComponent 
		{

	private static final String SCHEME = "id:";
	
	//private static final String RESOLVED_OBJECTS = "rslvd";
	
	private static final String HREF = "href";
	
	private static final String TITLE = "title";
	
	private static final String CLASS = "class";
	
	private static final String PROXY = "proxy";
	
	private static final String NO_PROXY = "noproxy";
	
	private static final int TITLE_COLUMN = 1;
	
	private static final int PROXY_COLUMN = 2;
	
	private static final int URL_COLUMN = 3;
	
	private static final int PUBLISHER_COLUMN = 4;
	
	private static final String SQL =
	"SELECT TITLE, PROXY, URL, PUBLISHER FROM ERESOURCE, VERSION, LINK " +
	"WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID " + 
	"AND VERSION.VERSION_ID = LINK.VERSION_ID " +
	"AND OBJECT_ID = ?";
	
	private static final String XPATH = "//*[name() = 'a' and @href[starts-with(.,'" + SCHEME + "')]]";
	
	private static final long DEFAULT_EXPIRATION = 60 * 60 * 1000;
	
	private String connectionName;
	
	private DataSourceComponent dataSource;
	
	private XPathProcessor xpathProcessor;
	
	private long expiration;

	protected Document transform(Document doc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		NodeList nodeList = null;
//		Request request = ObjectModelHelper.getRequest(super.objectModel);
//		Map resolvedObjects = (Map) request.getAttribute(RESOLVED_OBJECTS);
//		if (resolvedObjects == null) {
//			resolvedObjects = new HashMap();
//			request.setAttribute(RESOLVED_OBJECTS, resolvedObjects);
//		}
		nodeList = this.xpathProcessor.selectNodeList(doc,XPATH);
		if (nodeList.getLength() > 0) {
			try {
				conn = dataSource.getConnection();
				if (nodeList.getLength() > 0) {
					stmt = conn.prepareStatement(SQL);
					for (int i = 0; i < nodeList.getLength(); i++) {
						Element anchor = (Element) nodeList.item(i);
						Attr href = anchor.getAttributeNode(HREF);
						String idUri = href.getValue();
						stmt.setString(1,idUri.substring(idUri.indexOf(':') + 1));
						ResultSet rs = stmt.executeQuery();
						if (rs.next()) {
							String title = rs.getString(TITLE_COLUMN);
							String clazz = rs.getString(PROXY_COLUMN).equals("T") ? PROXY : NO_PROXY;
							String url = rs.getString(URL_COLUMN);
							String publisher = rs.getString(PUBLISHER_COLUMN);
							href.setValue(url);
						
							Attr titleAttr = anchor.getAttributeNode(TITLE);
							if (titleAttr == null) {
								titleAttr = anchor.getOwnerDocument().createAttribute(TITLE);
								StringBuffer titleAttrValue = new StringBuffer(title);
								if (publisher != null) {
									titleAttrValue.append(':').append(publisher);
								}
								titleAttr.setValue(titleAttrValue.toString());
								anchor.setAttributeNode(titleAttr);
							}
							titleAttr.setValue(title);
							Attr classAttr = anchor.getAttributeNode(CLASS);
							if (classAttr == null) {
								classAttr = anchor.getOwnerDocument().createAttribute(CLASS);
								anchor.setAttributeNode(classAttr);
							}
							classAttr.setValue(clazz);
							if (anchor.getChildNodes().getLength() == 0) {
								Text text = anchor.getOwnerDocument().createTextNode(title);
								anchor.appendChild(text);
							}
							if (getLogger().isDebugEnabled()) {
								getLogger().debug(idUri + " => " + title + "," + publisher + ", " + clazz + "," + url);
							}
						}
						rs.close();
					}
				}
			} catch (SQLException e) {
				getLogger().error(e.getMessage(),e);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						getLogger().error(e.getMessage(), e);
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						getLogger().error(e.getMessage(), e);
					}
				}
			}
		}
		return doc;
	}
	

	public Serializable getKey() {
		return "";
	}

	public SourceValidity getValidity() {
		return new ExpiresValidity(this.expiration);
	}

	public void parameterize(Parameters params) throws ParameterException {
		this.connectionName = params.getParameter("connection-name","eresources");
		this.expiration = params.getParameterAsLong("expiration", DEFAULT_EXPIRATION);
	}

	public void initialize() throws Exception {
		ExtendedComponentSelector selector = (ExtendedComponentSelector) super.manager.lookup(DataSourceComponent.ROLE + "Selector");
		this.dataSource = (DataSourceComponent) selector.select(this.connectionName);
		super.manager.release(selector);
		this.xpathProcessor = (XPathProcessor) this.manager.lookup(XPathProcessor.ROLE);
	}
	
	public void dispose() {
		this.manager.release(this.dataSource);
		this.manager.release((Component) this.xpathProcessor);
		this.dataSource = null;
		this.xpathProcessor = null;
		super.dispose();
	}

}
