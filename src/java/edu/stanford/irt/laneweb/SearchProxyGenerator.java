package edu.stanford.irt.laneweb;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.generation.WebServiceProxyGenerator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class SearchProxyGenerator extends WebServiceProxyGenerator {

    private static final String HTTP_CLIENT = "HTTP_CLIENT";

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
        return httpClient;
    }


}
