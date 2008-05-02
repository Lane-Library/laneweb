package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UrlTester extends ServiceableGenerator {

    private HttpClient httpClient;

    private String url;

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        MetaSearchManagerSource source = (MetaSearchManagerSource) this.manager
                .lookup(MetaSearchManagerSource.class.getName());
        this.httpClient = source.getHttpClient();
    }

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = (Request) objectModel
                .get(ObjectModelHelper.REQUEST_OBJECT);
        this.url = request.getParameter("url");
    }

    @Override
    public void recycle() {
        this.url = null;
        super.recycle();
    }

    public void generate() throws SAXException, IOException {
        GetMethod get = new GetMethod(this.url);
        this.httpClient.executeMethod(get);
        HTMLConfiguration config = new HTMLConfiguration();
        config.setFeature("http://xml.org/sax/features/namespaces", false);
        config.setProperty("http://cyberneko.org/html/properties/names/elems",
                "lower");
        DOMParser parser = new DOMParser(config);
        parser.parse(new InputSource(get.getResponseBodyAsStream()));

        DOMStreamer domStreamer = new DOMStreamer(this.contentHandler,
                this.lexicalHandler);
        this.contentHandler.startDocument();
        domStreamer.stream(parser.getDocument());
        this.contentHandler.endDocument();

    }

}
