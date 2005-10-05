package edu.stanford.laneweb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.transformation.AbstractSAXTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.lane.catalog.BLOBSource;
import edu.stanford.lane.catalog.MarcReader;
import edu.stanford.lane.marc4j.MARCtoSAXHandler;

public class EResourceTransformer extends AbstractSAXTransformer implements Initializable {
    
    private static final String LMLDB = "lmldb";

	private boolean voyagerDoc;
	private MarcReader myReader;
	private BLOBSource mySource;
	private MARCtoSAXHandler myHandler;

	public void setupTransforming()
		throws ProcessingException, IOException, SAXException {
		super.setupTransforming();
		voyagerDoc = false;
		try {
			mySource = (BLOBSource) manager.lookup(BLOBSource.ROLE);
			myReader = (MarcReader) manager.lookup(MarcReader.ROLE);
			myReader.setMarcHandler(myHandler);
		} catch (ServiceException e) {
			getLogger().error(e.getMessage(), e);
			throw new ProcessingException(e);
		}
	}

	public void startDocument() throws SAXException {
		if (!voyagerDoc) {
			super.startDocument();
		}
	}

	public void endDocument() throws SAXException {
		if (!voyagerDoc) {
			super.endDocument();
		}
	}

	public void startTransformingElement(
		String uri,
		String name,
		String raw,
		Attributes attr)
		throws ProcessingException, IOException, SAXException {
		this.xmlConsumer.startElement(uri, name, raw, attr);
		if (name.equals("li")) {
			voyagerDoc = true;
			String ids = attr.getValue("id");
			String bibid = null;
			String mfhdid = null;
			String authid = null;
			StringTokenizer st = new StringTokenizer(attr.getValue("id"), ":;");
			while (st.hasMoreTokens()) {
				String next = st.nextToken();
				if (next.equals("bib")) {
					bibid = st.nextToken();
				} else if (next.equals("mfhd")) {
					mfhdid = st.nextToken();
				} else if (next.equals("auth")) {
					authid = st.nextToken();
				}
			}
			if (bibid != null) {
//                if (this.request.getRequestURI().indexOf("org.xml") > 0) {
//                    myReader.parse(
//                            new InputStreamReader(
//                        new ByteArrayInputStream(
//                            mySource.getBlob("cifdb", BLOBSource.BIB, bibid)),"ISO8859_1"));
//                } else {
				myReader.parse(
						new InputStreamReader(
					new ByteArrayInputStream(
						mySource.getBlob(LMLDB, BLOBSource.BIB, bibid)),"ISO8859_1"));
//                }
			}
			if (mfhdid != null) {
				myReader.parse(
					new ByteArrayInputStream(
						mySource.getBlob(LMLDB, BLOBSource.MFHD, mfhdid)));
			}
			if (authid != null) {
				myReader.parse(
					new ByteArrayInputStream(
						mySource.getBlob(LMLDB, BLOBSource.AUTH, authid)));
			}
		}
	}
	
	public void endTransformingElement(String uri, String name, String raw)
		throws SAXException, ProcessingException {
			if (name.equals("li")) {
				voyagerDoc = false;
			}
		this.xmlConsumer.endElement(uri, name, raw);
	}
	
	public void recycle() {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("recycle()");
		}
		super.recycle();
		manager.release( mySource);
		manager.release( myReader);
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.activity.Initializable#initialize()
	 */
	public void initialize() throws Exception {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("initialize()");
		}
		defaultNamespaceURI = "http://www.w3.org/1999/xhtml";
		myHandler = new MARCtoSAXHandler();
		myHandler.enableLogging(getLogger().getChildLogger("marc-handler"));
		myHandler.setContentHandler(this);
	}
}
