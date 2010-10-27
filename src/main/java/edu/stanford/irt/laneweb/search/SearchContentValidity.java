package edu.stanford.irt.laneweb.search;

import java.io.File;
import java.util.List;

import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.FileTimeStampValidity;


public class SearchContentValidity implements SourceValidity{
    
    private static final long serialVersionUID = 1L;
    
    FileTimeStampValidity[] filesValidity;

    public SearchContentValidity(List<File> files){
        filesValidity = new FileTimeStampValidity[files.size()];
        int i = 0;
        for (File file : files) {
            this.filesValidity[i++] = new FileTimeStampValidity(file); 
        }
    }
    
    public int isValid() {
        for (FileTimeStampValidity validity : this.filesValidity) {
            if(validity.isValid() == -1)
                return -1;
        }
        return 1;
    }

    public int isValid(SourceValidity newValidity) {
        for (FileTimeStampValidity validity : this.filesValidity) {
            if(validity.isValid(newValidity) == -1)
                return -1;
        }
        return 1;
    }
}
