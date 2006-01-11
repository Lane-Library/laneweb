/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EresourcesQueryGenerator extends AbstractGenerator {
    
    private static final String TYPE = "t";
    private static final String MESH = "m";
    private static final String ALPHA = "a";
    private static final String TEXT = "q";
    private static final String SUBSET = "s";
    private static final String XMLNS = "http://apache.org/cocoon/SQL/2.0";
    private static final String EXECUTE_QUERY = "execute-query";
    private static final String QUERY = "query";
    private static final String SELECT_FROM = "SELECT " +
"DISTINCT LINK.LINK_ID, ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, ERESOURCE.TITLE, lower(ERESOURCE.TITLE) as LTITLE, VERSION.PUBLISHER, VERSION.HOLDINGS, VERSION.DATES, VERSION.DESCRIPTION, VERSION.PROXY, LINK.URL, LINK.LABEL, LINK.INSTRUCTION " +
"FROM " +
"ERESOURCE, VERSION, LINK, ERESOURCE_VERSION, VERSION_LINK";
    private static final String WHERE =
" WHERE " +
"ERESOURCE.ERESOURCE_ID = ERESOURCE_VERSION.ERESOURCE_ID " +
"AND " + 
"VERSION.VERSION_ID = ERESOURCE_VERSION.VERSION_ID " +
"AND " +
"VERSION.VERSION_ID = VERSION_LINK.VERSION_ID " +
"AND " + 
"LINK.LINK_ID = VERSION_LINK.LINK_ID";
    private static String UNION = " UNION ";
    private static final String ALT_SELECT_FROM = "SELECT " +
    "DISTINCT LINK.LINK_ID, ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, ERESOURCE.PREFERRED_TITLE, lower(ERESOURCE.PREFERRED_TITLE) as LTITLE, VERSION.PUBLISHER, VERSION.HOLDINGS, VERSION.DATES, VERSION.DESCRIPTION, VERSION.PROXY, LINK.URL, LINK.LABEL, LINK.INSTRUCTION " +
    "FROM " +
    "ERESOURCE, VERSION, LINK, ERESOURCE_VERSION, VERSION_LINK";
    private static final String ALT_AND = " AND ERESOURCE.PREFERRED_TITLE IS NOT NULL";
    private static final String ORDER = " ORDER BY LTITLE, VERSION_ID";
    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private String type;
    private String mesh;
    private String subset; 
    private String alpha;
    private String text;

    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters par) throws ProcessingException, SAXException,
            IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.type = request.getParameter(TYPE);
        if (this.type != null) {
            if (this.type.length() == 0) {
                this.type = null;
            } else {
                this.type = this.type.toLowerCase();
            }
        }
        this.mesh = request.getParameter(MESH);
        if (this.mesh != null) {
            if (this.mesh.length() == 0) {
                this.mesh = null;
            } else {
                this.mesh = this.mesh.toLowerCase();
            }
        }
        this.subset = request.getParameter(SUBSET);
        if (this.subset != null) {
            if (this.subset.length() == 0) {
                this.subset = null;
            } else {
                this.subset = this.subset.toLowerCase();
            }
        }
        this.alpha = request.getParameter(ALPHA);
        if (this.alpha != null) {
            if (this.alpha.length() == 0) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.toLowerCase();
            }
        }
        this.text = request.getParameter(TEXT);
        if (this.text != null) {
            if (this.text.length() ==0) {
                this.text = null;
            }
        }
    }

    public void generate() throws SAXException {
        StringBuffer query = new StringBuffer(SELECT_FROM);
        if (this.type != null) {
            query.append(", TYPE");
        }
        if (this.mesh != null) {
            query.append(", MESH");
        }
        if (this.subset != null) {
            query.append(", SUBSET");
        }
        query.append(WHERE);
        if (this.type != null) {
            query.append(" AND TYPE.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND TYPE.TYPE = '").append(this.type).append("'");
        }
        if (this.mesh != null) {
            query.append(" AND MESH.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND MESH.TERM = '").append(this.mesh).append("'");
        }
        if (this.subset != null) {
            query.append(" AND SUBSET.VERSION_ID = VERSION.VERSION_ID AND SUBSET.SUBSET = '").append(this.subset).append("'");
        }
        if (this.alpha != null) {
            query.append(" AND lower(ERESOURCE.TITLE) LIKE '").append(this.alpha).append("%'");
        }
        query.append(UNION);
        query.append(ALT_SELECT_FROM);
        if (this.type != null) {
            query.append(", TYPE");
        }
        if (this.mesh != null) {
            query.append(", MESH");
        }
        if (this.subset != null) {
            query.append(", SUBSET");
        }
        query.append(WHERE);
        query.append(ALT_AND);
        if (this.type != null) {
            query.append(" AND TYPE.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND TYPE.TYPE = '").append(this.type).append("'");
        }
        if (this.mesh != null) {
            query.append(" AND MESH.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND MESH.TERM = '").append(this.mesh).append("'");
        }
        if (this.subset != null) {
            query.append(" AND SUBSET.VERSION_ID = VERSION.VERSION_ID AND SUBSET.SUBSET = '").append(this.subset).append("'");
        }
        if (this.alpha != null) {
            query.append(" AND lower(ERESOURCE.PREFERRED_TITLE) LIKE '").append(this.alpha).append("%'");
        }
        query.append(ORDER);
        char[] queryArray = query.toString().toCharArray();
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(XMLNS,EXECUTE_QUERY,EXECUTE_QUERY,EMPTY_ATTS);
        this.xmlConsumer.startElement(XMLNS,QUERY,QUERY,EMPTY_ATTS);
        this.xmlConsumer.characters(queryArray,0,queryArray.length);
        this.xmlConsumer.endElement(XMLNS,QUERY,QUERY);
        this.xmlConsumer.endElement(XMLNS,EXECUTE_QUERY,EXECUTE_QUERY);
        this.xmlConsumer.endDocument();
    }
    
    public void recycle() {
        this.type = null;
        this.mesh = null;
        this.subset = null;
        this.alpha = null;
        this.text = null;
    }

}
