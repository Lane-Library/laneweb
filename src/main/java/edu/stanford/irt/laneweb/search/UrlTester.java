package edu.stanford.irt.laneweb.search;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sun.imageio.metadata.XmlChars;

public class UrlTester  extends ServiceableGenerator {

    private HttpClient httpClient;

    private String url;


    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        MetaSearchManagerSource source = (MetaSearchManagerSource) this.manager.lookup(MetaSearchManagerSource.class.getName());
        this.httpClient = source.getHttpClient();
    }

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = (Request) objectModel.get(ObjectModelHelper.REQUEST_OBJECT);
        this.url = request.getParameter("url");
	    }

	    @Override
	    public void recycle() {
	        this.url = null;
	        super.recycle();
	    }

	    public void generate() throws SAXException, IOException {
	    	 GetMethod get = new GetMethod(url);
	    	httpClient.executeMethod(get);
	    	Tidy tidy = new Tidy();
	        tidy.setXmlOut(true);
	        tidy.setNumEntities(true);
	        tidy.setShowWarnings(false);
	        tidy.setQuiet(true);
	        tidy.setXHTML(true);
            StringWriter stringWriter = new StringWriter();
            PrintWriter errorWriter = new PrintWriter(stringWriter);
            tidy.setErrout(errorWriter);
            //ByteArrayOutputStream output = new ByteArrayOutputStream();

            org.w3c.dom.Document doc = tidy.parseDOM(new BufferedInputStream(new BufferedInputStream(get.getResponseBodyAsStream())), null);
            XMLUtils.stripDuplicateAttributes(doc, null);
            get.releaseConnection();
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

	    }
	
}
