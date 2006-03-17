package edu.stanford.laneweb;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Map;
import java.util.Properties;

import org.apache.excalibur.xml.sax.SAXParser;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.modules.input.PropertiesFileModule;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HtmlProxyGenerator extends SearchProxyGenerator {

	/** The HTTP method to use at request time. */

	public void generate() throws IOException, SAXException,
			ProcessingException {
		Tidy tidy = new Tidy();
		tidy.setXmlOut(true);
		tidy.setXHTML(true);

		// Set Jtidy warnings on-off
		tidy.setShowWarnings(getLogger().isWarnEnabled());
		// Set Jtidy final result summary on-off
		tidy.setQuiet(!getLogger().isInfoEnabled());
		// Set Jtidy infos to a String (will be logged) instead of System.out
		StringWriter stringWriter = new StringWriter();
		PrintWriter errorWriter = new PrintWriter(stringWriter);
		tidy.setErrout(errorWriter);

		// Extract the document using JTidy and stream it.

		SAXParser parser = null;
		try {
			if (this.getLogger().isDebugEnabled()) {
				this.getLogger().debug(
						"processing Web Service request: " + this.source);
			}

			// forward request and bring response back
			byte[] response = this.fetch();
			if (this.getLogger().isDebugEnabled()) {
				this.getLogger().debug("response: " + new String(response));
			}

			/*
			 * TODO: Though I avoided the getResponseBodyAsString(), the content
			 * seems not to be parsed correctly. Who cares about the encoding in
			 * the XML declaration?
			 * {@link http://jakarta.apache.org/commons/httpclient/apidocs/org/apache/commons/httpclient/HttpMethodBase.html#getResponseBodyAsString()}
			 */
			ByteArrayInputStream responseStream = new ByteArrayInputStream(
					response);
			org.w3c.dom.Document doc = tidy.parseDOM(new BufferedInputStream(
					responseStream), null);

			// FIXME: Jtidy doesn't warn or strip duplicate attributes in same
			// tag; stripping.
			XMLUtils.stripDuplicateAttributes(doc, null);

			errorWriter.flush();
			errorWriter.close();
			if (getLogger().isWarnEnabled()) {
				getLogger().warn(stringWriter.toString());
			}

			DOMStreamer domStreamer = new DOMStreamer(this.contentHandler,
					this.lexicalHandler);
			this.contentHandler.startDocument();

			domStreamer.stream(doc.getDocumentElement());
			this.contentHandler.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.manager.release(parser);
		}

	} // generate

}
