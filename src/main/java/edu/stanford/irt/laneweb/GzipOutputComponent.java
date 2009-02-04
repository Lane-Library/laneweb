package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.cocoon.sitemap.SitemapOutputComponent;
import org.xml.sax.SAXException;

public class GzipOutputComponent implements SitemapModelComponent,
		SitemapOutputComponent {
	
	private String mimeType;
	
	private boolean isGzip = false;
	
	protected OutputStream outputStream;

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		String mimeType = par.getParameter("mime-type", null);
		if (null != mimeType) {
			this.mimeType = mimeType;
		}
		HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
		String acceptEncoding = request.getHeader("Accept-Encoding");
		if (null != acceptEncoding) {
			for (StringTokenizer st = new StringTokenizer(acceptEncoding,",");st.hasMoreTokens();) {
				if ("gzip".equals(st.nextToken().trim())) {
					this.isGzip = true;
					break;
				}
			}
		}
		if (this.isGzip) {
			HttpServletResponse response = ObjectModelHelper.getResponse(objectModel);
			response.setHeader("Content-Encoding", "gzip");
		}
	}

	public String getMimeType() {
		return this.mimeType;
	}
	
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}
	
	public boolean isGzip() {
	    return this.isGzip;
	}

	public void setOutputStream(OutputStream out) throws IOException {
		if (this.isGzip) {
			this.outputStream = new GZIPOutputStream(out);
		} else {
			this.outputStream = out;
		}
	}

	public boolean shouldSetContentLength() {
		return false;
	}

}
