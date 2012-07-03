package edu.stanford.irt.laneweb.search;

import java.io.File;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;

public class SearchDirectoryTransformer extends AbstractTransformer implements ParametersAware, CacheableProcessingComponent {

    private static final String FILE = "file";

    private static final String NAMESPACE = "http://lane.stanford.edu/search-templates/ns";

    private static final String SEARCH_TEMPLATES = "search-templates";

    private static final String TYPE = "search-directory";

    private String[] directories;

    @Override
    public void endDocument() throws SAXException {
        for (String directory : this.directories) {
            parseDirectory(new File(directory.substring(directory.indexOf(':') + 1)));
        }
        endElement(NAMESPACE, SEARCH_TEMPLATES, SEARCH_TEMPLATES);
        super.endDocument();
    }

    public String getKey() {
        return TYPE;
    }

    public String getType() {
        return TYPE;
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.directories = parameters.get("directories").split(",");
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        startElement(NAMESPACE, SEARCH_TEMPLATES, SEARCH_TEMPLATES, new AttributesImpl());
    }

    private void parseDirectory(final File directory) throws SAXException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
                parseDirectory(file);
            } else if (file.isFile() && file.canRead() && file.getName().endsWith(".html")) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "path", "path", "CDATA", file.getAbsolutePath());
                startElement("", FILE, FILE, attributes);
                endElement("", FILE, FILE);
            }
        }
    }
}
