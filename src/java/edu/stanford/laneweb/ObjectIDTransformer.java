package edu.stanford.laneweb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.ExtendedComponentSelector;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
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
	
	private static final String RESOLVED_OBJECTS = "rslvd";
	
	private static final String HREF = "href";
	
	private static final String TITLE = "title";
	
	private static final String CLASS = "class";
	
	private static final String PROXY = "proxy";
	
	private static final String NO_PROXY = "noproxy";
	
	private static final int TITLE_COLUMN = 1;
	
	private static final int PROXY_COLUMN = 2;
	
	private static final int URL_COLUMN = 3;
	
	private static final int PUBLISHER_COLUMN = 4;
	
	private static final int OBJECT_ID_COLUMN = 5;
	
	private static final String SQL =
	"SELECT TITLE, PROXY, URL, PUBLISHER, OBJECT_ID FROM ERESOURCE, VERSION, LINK " +
	"WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID " + 
	"AND VERSION.VERSION_ID = LINK.VERSION_ID " +
	"AND OBJECT_ID = '";
	
	private static final String XPATH = "//*[name() = 'a' and @href[starts-with(.,'" + SCHEME + "')]]";
	
	private static final long DEFAULT_EXPIRATION = 60 * 60 * 1000;
	
	private String connectionName;
	
	private DataSourceComponent dataSource;
	
	private XPathProcessor xpathProcessor;
	
	private long expiration;

	protected Document transform(Document doc) {
		NodeList nodeList = this.xpathProcessor.selectNodeList(doc,XPATH);
		if (nodeList.getLength() == 0) {
			return doc;
		}
		Request request = ObjectModelHelper.getRequest(super.objectModel);
		Map resolvedObjects = (Map) request.getAttribute(RESOLVED_OBJECTS);
		if (resolvedObjects == null) {
			resolvedObjects = new HashMap();
			request.setAttribute(RESOLVED_OBJECTS, resolvedObjects);
		}
		Set ids = new HashSet();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element anchor = (Element) nodeList.item(i);
			Attr href = anchor.getAttributeNode(HREF);
			String idUri = href.getValue();
			ids.add(idUri.substring(SCHEME.length()));
		}
		ids.removeAll(resolvedObjects.keySet());
		if (ids.size() > 0) {
			StringBuffer sql = new StringBuffer();
			for (Iterator it = ids.iterator(); it.hasNext();) {
				sql.append(SQL).append(it.next()).append("\'\n");
				if (it.hasNext()) {
					sql.append("UNION\n");
				}
			}
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = dataSource.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql.toString());
				while (rs.next()) {
					ResolvableObject resolvableObject = new ResolvableObject();
					resolvableObject.title = rs.getString(TITLE_COLUMN);
					resolvableObject.proxy = rs.getString(PROXY_COLUMN).equals("T") ? true : false;
					resolvableObject.url = rs.getString(URL_COLUMN);
					resolvableObject.publisher = rs.getString(PUBLISHER_COLUMN);
					resolvableObject.id = rs.getString(OBJECT_ID_COLUMN);
					resolvedObjects.put(resolvableObject.id, resolvableObject);
				}
			} catch (SQLException e) {
				getLogger().error(e.getMessage(), e);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						getLogger().error(e.getMessage(), e);
					}
				}
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
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element anchor = (Element) nodeList.item(i);
			Attr href = anchor.getAttributeNode(HREF);
			String id = href.getValue().substring(SCHEME.length());
			ResolvableObject resolvableObject = (ResolvableObject) resolvedObjects.get(id);
			if (resolvableObject != null) {
				href.setValue(resolvableObject.url);
				Attr titleAttr = anchor.getAttributeNode(TITLE);
				if (titleAttr == null) {
					titleAttr = doc.createAttribute(TITLE);
					anchor.setAttributeNode(titleAttr);
				}
				StringBuffer titleAttrValue = new StringBuffer(resolvableObject.title);
				if (resolvableObject.publisher != null) {
					titleAttrValue.append(':').append(resolvableObject.publisher);
				}
				titleAttr.setValue(titleAttrValue.toString());
				Attr classAttr = anchor.getAttributeNode(CLASS);
				if (classAttr == null) {
					classAttr = doc.createAttribute(CLASS);
					anchor.setAttributeNode(classAttr);
				}
				classAttr.setValue(resolvableObject.proxy ? PROXY : NO_PROXY);
				if (anchor.getChildNodes().getLength() == 0) {
					Text text = doc.createTextNode(resolvableObject.title);
					anchor.appendChild(text);
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
	
	private class ResolvableObject {
		protected String id;
		protected String url;
		protected String title;
		protected String publisher;
		protected boolean proxy;
	}

}
