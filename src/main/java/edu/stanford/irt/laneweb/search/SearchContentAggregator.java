package edu.stanford.irt.laneweb.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;

public class SearchContentAggregator extends AbstractGenerator implements CacheableProcessingComponent{

    String ns = "http://lane.stanford.edu/search-templates/ns";
    String rootElement = "search-templates";
    
    
    private List<File> files = null;

    public void generate() throws IOException, SAXException, ProcessingException {
        super.xmlConsumer.startDocument();
        super.xmlConsumer.startElement(this.ns, this.rootElement,this.rootElement, new AttributesImpl());
        
        
        XMLReader domReader = XMLReaderFactory.createXMLReader();
        SearchContentXMLFilter domParser = new SearchContentXMLFilter(domReader);
        domParser.setContentHandler(this.xmlConsumer);
        InputSource in = new InputSource(this.source.getInputStream());
        domParser.parse(in);
        
        XMLReader htmlReader = XMLReaderFactory.createXMLReader("org.cyberneko.html.parsers.SAXParser");
        htmlReader.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        SearchContentXMLFilter parser = new SearchContentXMLFilter(htmlReader);
        parser.setContentHandler(this.xmlConsumer);
        FileInputStream fileInput = null;
        try {
            for (File file : files) {
                fileInput = new FileInputStream(file);
                in = new InputSource(fileInput);
                parser.parse(in);
            }
        } finally {
            if (fileInput != null)
                fileInput.close();
        }
        super.xmlConsumer.endElement(this.ns, this.rootElement, this.rootElement);
        super.xmlConsumer.endDocument();
    }

    @Override
    protected void initialize() {
        this.files = new ArrayList<File>();
        String path =  model.get(Model.DEFAULT_CONTENT_BASE).toString().substring(5);
        String[] directories = this.parameterMap.get("content-directory").split(",");
        for (String directory : directories) {
            setFiles(path.concat("/").concat(directory));
        }
    }

    private void setFiles(String path) {
        File directory = new File(path);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file.isDirectory())
                setFiles(file.getPath());
            if (file.isFile() && file.canRead() && file.getName().endsWith(".html")) {
                files.add(file);
            }
        }
    }
    

    /**
     * Generate the unique key. This key must be unique inside the space of this component. This method must be invoked
     * before the generateValidity() method.
     * 
     * @return The generated key or <code>0</code> if the component is currently not cacheable.
     */
    public java.io.Serializable getKey() {
        return this.source.getURI();
    }

    /**
     * Generate the validity object. Before this method can be invoked the generateKey() method must be invoked.
     * 
     * @return The generated validity object or <code>null</code> if the component is currently not cacheable.
     */
    public SourceValidity getValidity() {
        return this.source.getValidity();
    }
}
