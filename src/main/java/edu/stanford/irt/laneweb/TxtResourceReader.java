package edu.stanford.irt.laneweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;

import edu.stanford.irt.laneweb.cocoon.AbstractReader;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class TxtResourceReader extends AbstractReader implements CacheableProcessingComponent {

    private String defaultPath;

    private String valueToSubstitute;

    protected String path;

    public void generate() throws IOException {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(this.source.getInputStream()));
            String line = null;
            while ((line = bf.readLine()) != null) {
                line = line.replaceAll(this.valueToSubstitute, this.path);
                this.outputStream.write(line.getBytes());
                this.outputStream.write('\n');
            }
            this.outputStream.flush();
        } finally {
            if (bf != null) {
                bf.close();
            }
        }
    }

    public Serializable getKey() {
        return this.source.getURI() + ";path=" + this.path;
    }

    public long getLastModified() {
        return this.source.getLastModified();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }

    public void setDefaultPath(final String path) {
        this.defaultPath = path;
    }

    public void initialize() {
        //get the path from a sitemap parameter or the base-path from the model, or the default
        this.path = this.parameterMap.containsKey("path") ?
                this.parameterMap.get("path") :
                    this.model.getString(LanewebObjectModel.BASE_PATH, this.defaultPath);
    }

    public void setValueToSubstitute(final String valueToSubstitute) {
        this.valueToSubstitute = valueToSubstitute;
    }
}
