package edu.stanford.laneweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.Configuration;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;

import org.apache.cocoon.environment.SourceResolver;


import org.apache.cocoon.reading.ResourceReader;

import org.xml.sax.SAXException;

public class TxtResourceReader extends ResourceReader {

	String configuredPath = null;

	String path = null;

	String valueToSubtitute = null;

	public void configure(Configuration configuration)
			throws ConfigurationException {
		super.configure(configuration);
		this.configuredPath = configuration.getChild("path").getValue();
		this.valueToSubtitute = configuration.getChild("valueToSubtitute")
				.getValue();
	}

	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		super.setup(resolver, objectModel, src, par);
		this.path = par.getParameter("path", configuredPath);
	}

	protected void processStream(InputStream inputStream) throws IOException,
			ProcessingException {
		byte[] buffer = new byte[bufferSize];
		String ranges = request.getHeader("Range");
		long contentLength = inputSource.getContentLength();

		if (byteRanges && ranges != null) {
			throw new ProcessingException("Reader only for Text file");
		} else {
			if (contentLength != -1) {
				response.setHeader("Content-Length", Long
						.toString(contentLength));
			}
			StringBuffer page = new StringBuffer();
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = null;
			while ((line = bf.readLine()) != null) {
				page.append(line);
				page.append("\n");
			}
			String pageToSend = page.toString().replaceAll(valueToSubtitute, path);
			buffer = pageToSend.getBytes();
			out.write(buffer, 0, buffer.length);
			out.flush();
		}
	}
}
