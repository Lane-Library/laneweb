/*
 * Created on Sep 5, 2003
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.stanford.lane.marc4j;

import java.io.UnsupportedEncodingException;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.marc4j.MarcHandler;
import org.marc4j.marc.Leader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author ceyates
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MARCtoSAXHandler
	extends AbstractLogEnabled
	implements MarcHandler {

	private static final String NAMESPACE = "http://www.loc.gov/MARC21/slim";
    private static final String DEFAULT_NAMESPACE = "";
	private static final String COLLECTION = "collection";
	private static final String RECORD = "record";
	private static final String LEADER = "leader";
	private static final String CONTROLFIELD = "controlfield";
	private static final String DATAFIELD = "datafield";
	private static final String SUBFIELD = "subfield";
	private static final String TAG = "tag";
	private static final String IND1 = "ind1";
	private static final String IND2 = "ind2";
	private static final String CODE = "code";
	private static final String CDATA = "CDATA";
	private static final Attributes EMPTY_ATTS = new AttributesImpl();

	private ContentHandler myHandler;

	public void setContentHandler(ContentHandler aHandler) {
		myHandler = aHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#startCollection()
	 */
	public void startCollection() {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("startCollection()");
		}
		try {
			myHandler.startDocument();
			myHandler.startPrefixMapping("", NAMESPACE);
			myHandler.startElement(
				NAMESPACE,
				COLLECTION,
				COLLECTION,
				EMPTY_ATTS);
		} catch (SAXException e) {
			if (getLogger().isErrorEnabled()) {
				getLogger().error(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#endCollection()
	 */
	public void endCollection() {
		if (getLogger().isDebugEnabled()) {

			getLogger().debug("endCollection()");
		}
		try {
			myHandler.endElement(NAMESPACE, COLLECTION, COLLECTION);
			myHandler.endPrefixMapping("");
			myHandler.endDocument();
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#startRecord(org.marc4j.marc.Leader)
	 */
	public void startRecord(Leader leader) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("startRecord(" + leader.marshal() + ")");
		}
		try {
			myHandler.startElement(NAMESPACE, RECORD, RECORD, EMPTY_ATTS);
			myHandler.startElement(NAMESPACE, LEADER, LEADER, EMPTY_ATTS);
			char[] data = leader.marshal().toCharArray();
			myHandler.characters(data, 0, data.length);
			myHandler.endElement(NAMESPACE, LEADER, LEADER);
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#endRecord()
	 */
	public void endRecord() {
		if (getLogger().isDebugEnabled()) {

			getLogger().debug("endRecord()");
		}
		try {
			myHandler.endElement(NAMESPACE, RECORD, RECORD);
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#controlField(java.lang.String, char[])
	 */
	public void controlField(String tag, char[] data) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug(
				"controlField(tag=" + tag + " data=" + new String(data) + ")");
		}
		try {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute(DEFAULT_NAMESPACE, TAG, TAG, CDATA, tag);
			myHandler.startElement(NAMESPACE, CONTROLFIELD, CONTROLFIELD, atts);
			myHandler.characters(data, 0, data.length);
			myHandler.endElement(NAMESPACE, CONTROLFIELD, CONTROLFIELD);
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#startDataField(java.lang.String, char, char)
	 */
	public void startDataField(String tag, char ind1, char ind2) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug(
				"startDataField(tag="
					+ tag
					+ " ind1="
					+ ind1
					+ " ind2="
					+ ind2
					+ ")");
		}
		try {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute(DEFAULT_NAMESPACE, TAG, TAG, CDATA, tag);
			atts.addAttribute(
				DEFAULT_NAMESPACE,
				IND1,
				IND1,
				CDATA,
				new String(new char[] { ind1 }));
			atts.addAttribute(
				DEFAULT_NAMESPACE,
				IND2,
				IND2,
				CDATA,
				new String(new char[] { ind2 }));
			myHandler.startElement(NAMESPACE, DATAFIELD, DATAFIELD, atts);
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#endDataField(java.lang.String)
	 */
	public void endDataField(String tag) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("endDataField()");
		}
		try {
			myHandler.endElement(NAMESPACE, DATAFIELD, DATAFIELD);
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marc4j.MarcHandler#subfield(char, char[])
	 */
	public void subfield(char code, char[] data) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug(
				"subfield(code=" + code + " data=" + new String(data) + ")");
		}
		try {
			AttributesImpl atts = new AttributesImpl();
			atts.addAttribute(
				DEFAULT_NAMESPACE,
				CODE,
				CODE,
				CDATA,
				new String(new char[] { code }));
			myHandler.startElement(NAMESPACE, SUBFIELD, SUBFIELD, atts);
                        data = new String(new String(data).getBytes("ISO8859_1"),"UTF-8").toCharArray();
			myHandler.characters(data, 0, data.length);
			myHandler.endElement(NAMESPACE, SUBFIELD, SUBFIELD);
		} catch (SAXException e) {
			getLogger().error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
                        getLogger().error(e.getMessage(), e);
                }
	}

}
