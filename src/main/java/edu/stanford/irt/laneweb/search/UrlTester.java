package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.HTMLGenerator;

public class UrlTester extends HTMLGenerator {

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        String url = par.getParameter("url", null);
        super.setup(resolver, objectModel, url, par);
    }

}
