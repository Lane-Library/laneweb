package edu.stanford.irt.laneweb.search;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.core.xml.SAXParser;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.ContentAggregator;
import edu.stanford.irt.laneweb.model.Model;

public class SearchContentAggregator extends ContentAggregator {

    private static final String NAMESPACE = "http://lane.stanford.edu/search-templates/ns";

    private static final String SEARCH_TEMPLATES = "search-templates";

    public SearchContentAggregator(final SAXParser saxParser) {
        super(saxParser);
        setRootElement(SEARCH_TEMPLATES, NAMESPACE, "");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setup(final org.apache.cocoon.environment.SourceResolver resolver, final Map objectModel, final String src,
            final Parameters par) throws ProcessingException, SAXException, IOException {
        addPart(src, "", "", "false", "");
        URL contentBase = (URL) objectModel.get(Model.CONTENT_BASE);
        String basePath = contentBase.getPath();
        String[] directories = par.getParameter("directories", "").split(",");
        for (String directory : directories) {
            setFiles(basePath.length(), basePath.concat("/").concat(directory));
        }
        super.setup(resolver, objectModel, src, par);
    }

    private void setFiles(final int basePathLength, final String filePath) throws IOException {
        File directory = new File(filePath);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
                setFiles(basePathLength, file.getPath());
            } else if (file.isFile() && file.canRead() && file.getName().endsWith(".html")) {
                String cocoonSourceString = "content:/cached" + file.getAbsolutePath().substring(basePathLength);
                addPart(cocoonSourceString, "", "", "false", "");
            }
        }
    }
}
