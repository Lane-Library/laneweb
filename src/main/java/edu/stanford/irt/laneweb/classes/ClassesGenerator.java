package edu.stanford.irt.laneweb.classes;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.excalibur.source.SourceValidity;

import edu.stanford.irt.laneweb.cocoon.URLGenerator;

public class ClassesGenerator extends URLGenerator implements CacheableProcessingComponent {

    
   
    public ClassesGenerator(SAXParser saxParser){
        super.setParser(saxParser);        
    }
    
      
    public SourceValidity getValidity() {
        return new ClassesValidity();
    }


    
}
