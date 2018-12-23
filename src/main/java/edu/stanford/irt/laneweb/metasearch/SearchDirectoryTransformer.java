package edu.stanford.irt.laneweb.metasearch;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractCacheableTransformer;

public class SearchDirectoryTransformer extends AbstractCacheableTransformer {

    private static final String FILE = "file";

    private static final FileFilter FILE_FILTER = 
            (final File file) -> file.isFile() && file.canRead() && file.getName().endsWith(".html");

    private static final String NAMESPACE = "http://lane.stanford.edu/search-templates/ns";

    private static final String SEARCH_TEMPLATES = "search-templates";

    private static final String TYPE = "search-directory";

    private String[] directories;

    public SearchDirectoryTransformer() {
        super(TYPE);
    }

    @Override
    public void endDocument() throws SAXException {
        for (String directory : this.directories) {
            parseDirectory(new File(directory.substring(directory.indexOf(':') + 1)));
        }
        endElement(NAMESPACE, SEARCH_TEMPLATES, SEARCH_TEMPLATES);
        super.endDocument();
    }

    @Override
    @Deprecated
    public void setParameters(final Map<String, String> parameters) {
        this.directories = parameters.get("directories").split(",");
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        startElement(NAMESPACE, SEARCH_TEMPLATES, SEARCH_TEMPLATES, new AttributesImpl());
    }

    private void parseDirectory(final File directory) throws SAXException {
        File[] files = directory.listFiles(FILE_FILTER);
        if (files != null) {
            for (File file : files) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "path", "path", "CDATA", file.getAbsolutePath());
                startElement("", FILE, FILE, attributes);
                endElement("", FILE, FILE);
            }
        }
    }
}
