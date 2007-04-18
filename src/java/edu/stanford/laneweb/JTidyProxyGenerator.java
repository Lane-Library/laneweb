package edu.stanford.laneweb;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.generation.WebServiceProxyGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.excalibur.xml.sax.SAXParser;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JTidyProxyGenerator extends WebServiceProxyGenerator {
	
    private static final String HTTP_CLIENT = "HTTP_CLIENT";
    
    private HttpClient httpClient;
    
    public void generate() throws IOException, SAXException, ProcessingException {
//        SAXParser parser = null;
//        try {
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("processing Web Service request: " + this.source);
            }

            // forward request and bring response back
            byte[] response = this.fetch();
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("response: " + new String(response));
            }
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

            org.w3c.dom.Document doc = tidy.parseDOM(new BufferedInputStream(new ByteArrayInputStream(response)), null);

            // FIXME: Jtidy doesn't warn or strip duplicate attributes in same
            // tag; stripping.
            XMLUtils.stripDuplicateAttributes(doc, null);
            //tidy.parse(new ByteArrayInputStream(response), output);
            errorWriter.flush();
            errorWriter.close();
            if (getLogger().isWarnEnabled()) {
                getLogger().warn(stringWriter.toString());
            }
            //getLogger().debug("xhtml: " + new String(output.toByteArray()));
            //ByteArrayInputStream responseStream = new ByteArrayInputStream(output.toByteArray());


            DOMStreamer domStreamer = new DOMStreamer(this.contentHandler,
                                                      this.lexicalHandler);
            this.contentHandler.startDocument();
//            InputSource inputSource = new InputSource(responseStream);
//            parser = (SAXParser)this.manager.lookup(SAXParser.ROLE);
//            parser.parse(inputSource, super.xmlConsumer);
            domStreamer.stream(doc.getDocumentElement());
            this.contentHandler.endDocument();
//        } catch (ServiceException ex) {
//            throw new ProcessingException("JTidyProxyGenerator.generate() error", ex);
//        } finally {
//            this.manager.release(parser);
//        }

    } // generate

    protected HttpClient getHttpClient() {
        Request request = ObjectModelHelper.getRequest(objectModel);
        Session session = request.getSession(true);
        HttpClient httpClient = null;
        if (session != null) {
            httpClient = (HttpClient)session.getAttribute(HTTP_CLIENT);
        }
        if (httpClient == null) {
            httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
            session.setAttribute(HTTP_CLIENT, httpClient);
        }
        this.httpClient = httpClient;
        
        return httpClient;
    }
    
    public byte[] fetch() throws ProcessingException {
        HttpMethod method = null;

        // check which method (GET or POST) to use.
//        if (this.configuredHttpMethod.equalsIgnoreCase(METHOD_POST)) {
//            method = new PostMethod(this.source);
//        } else {
            method = new GetMethod(this.source);
//        }

//        if (this.getLogger().isDebugEnabled()) {
//            this.getLogger().debug("request HTTP method: " + method.getName());
//        }

        // this should probably be exposed as a sitemap option
        method.setFollowRedirects(true);

        // copy request parameters and merge with URL parameters
        Request request = ObjectModelHelper.getRequest(objectModel);

        ArrayList paramList = new ArrayList();
        Enumeration enumeration = request.getParameterNames();
//        while (enumeration.hasMoreElements()) {
//            String pname = (String)enumeration.nextElement();
//            String[] paramsForName = request.getParameterValues(pname);
//            for (int i = 0; i < paramsForName.length; i++) {
//                NameValuePair pair = new NameValuePair(pname, paramsForName[i]);
//                paramList.add(pair);
//            }
//        }
//
//        if (paramList.size() > 0) {
//            NameValuePair[] allSubmitParams = new NameValuePair[paramList.size()];
//            paramList.toArray(allSubmitParams);
//
//            String urlQryString = method.getQueryString();
//
//            // use HttpClient encoding routines
//            method.setQueryString(allSubmitParams);
//            String submitQryString = method.getQueryString();
//
//            // set final web service query string
//            
//            // sometimes the querystring is null here...
//            if (null == urlQryString) {
//                method.setQueryString(submitQryString);
//            } else {
//                method.setQueryString(urlQryString + "&" + submitQryString);	
//            }
//            
//        } // if there are submit parameters

        byte[] response = null;
        try {
            int httpStatus = httpClient.executeMethod(method);
            response = method.getResponseBody();
            if (httpStatus < 400) {
                if (this.getLogger().isDebugEnabled()) {
                    this.getLogger().debug("Return code when accessing the remote Url: " + httpStatus);
                }
            } else {
                throw new ProcessingException("The remote returned error " + httpStatus + " when attempting to access remote URL:" + method.getURI());
            }
        } catch (URIException e) {
            throw new ProcessingException("There is a problem with the URI: " + this.source, e);
        } catch (IOException e) {
            try {
                throw new ProcessingException("Exception when attempting to access the remote URL: " + method.getURI(), e);
            } catch (URIException ue) {
                throw new ProcessingException("There is a problem with the URI: " + this.source, ue);
            }
        } finally {
            /* It is important to always read the entire response and release the
             * connection regardless of whether the server returned an error or not.
             * {@link http://jakarta.apache.org/commons/httpclient/tutorial.html}
             */
            method.releaseConnection();
        }

        return response;
    } // fetch

}
