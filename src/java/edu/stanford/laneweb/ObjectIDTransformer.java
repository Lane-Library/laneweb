package edu.stanford.laneweb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.ExtendedComponentSelector;
import org.apache.cocoon.transformation.AbstractDOMTransformer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ObjectIDTransformer extends AbstractDOMTransformer 
	implements Parameterizable, Initializable, CacheableProcessingComponent 
		{

	private static final String OBJECT_ID_SCHEME = "id:";
	
	private static final String OBJECT_ALIAS_SCHEME = "alias:";
	
	private static final String OBJECT_ID_SQL = "SELECT URL FROM LINK WHERE OBJECT_ID = ?";
	
	private static final String OBJECT_ALIAS_SQL = "SELECT URL FROM LINK WHERE ALIAS = ?";
	
	private static final String OBJECT_ID_XPATH = "//@href[starts-with(.,'" + OBJECT_ID_SCHEME + "')]";
	
	private static final String OBJECT_ALIAS_XPATH = "//@href[starts-with(.,'" + OBJECT_ALIAS_SCHEME + "')]";
	
	private String connectionName;
	
	private DataSourceComponent dataSource;

	@Override
	protected Document transform(Document doc) {
		Connection conn = null;
		PreparedStatement idStatement = null;
		PreparedStatement aliasStatement = null;
		NodeList idNodeList = null;
		NodeList aliasNodeList = null;
		try {
			idNodeList = XPathAPI.selectNodeList(doc,OBJECT_ID_XPATH);
			aliasNodeList = XPathAPI.selectNodeList(doc,OBJECT_ALIAS_XPATH);
		} catch (TransformerException e) {
			getLogger().error(e.getMessage(), e);
			return doc;
		}
		if (idNodeList.getLength() > 0 || aliasNodeList.getLength() > 0) {
			try {
				conn = dataSource.getConnection();
				if (idNodeList.getLength() > 0) {
					idStatement = conn.prepareStatement(OBJECT_ID_SQL);
					for (int i = 0; i < idNodeList.getLength(); i++) {
						Attr att = (Attr) idNodeList.item(i);
						String value = att.getValue();
						idStatement.setString(1,value.substring(OBJECT_ID_SCHEME.length()));
						ResultSet rs = idStatement.executeQuery();
						if (rs.next()) {
							String href = rs.getString(1);
							att.setValue(href);
							if (getLogger().isDebugEnabled()) {
								getLogger().debug("replacing " + OBJECT_ID_SCHEME + value + " with url " + href);
							}
						}
						rs.close();
					}
				}
				if (aliasNodeList.getLength() > 0) {
					aliasStatement = conn.prepareStatement(OBJECT_ALIAS_SQL);
					for (int i = 0; i < aliasNodeList.getLength(); i++) {
						Attr att = (Attr) aliasNodeList.item(i);
						String value = att.getValue();
						aliasStatement.setString(1,value.substring(OBJECT_ALIAS_SCHEME.length()));
						ResultSet rs = aliasStatement.executeQuery();
						if (rs.next()) {
							String href = rs.getString(1);
							att.setValue(href);
							if (getLogger().isDebugEnabled()) {
								getLogger().debug("replacing " + OBJECT_ALIAS_SCHEME + value + " with url " + href);
							}
						}
						rs.close();
					}
				}
			} catch (SQLException e) {
				getLogger().error(e.getMessage(),e);
			} finally {
				if (idStatement != null) {
					try {
						idStatement.close();
					} catch (SQLException e) {
						getLogger().error(e.getMessage(), e);
					}
				}
				if (aliasStatement != null) {
					try {
						aliasStatement.close();
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
		return new SourceValidity () {

			public int isValid() {
				return SourceValidity.VALID;
			}

			public int isValid(SourceValidity arg0) {
				return SourceValidity.VALID;
			}
			
		};
	}

	public void parameterize(Parameters params) throws ParameterException {
		this.connectionName = params.getParameter("connection-name","eresources");
	}

	public void initialize() throws Exception {
		ExtendedComponentSelector selector = (ExtendedComponentSelector) super.manager.lookup(DataSourceComponent.ROLE + "Selector");
		this.dataSource = (DataSourceComponent) selector.select(this.connectionName);
		super.manager.release(selector);
	}

}
