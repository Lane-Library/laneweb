package edu.stanford.irt.laneweb.cocoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class TxtResourceReader extends AbstractReader implements CacheableProcessingComponent {

    private static final String BASEPATH_SUBSTITUTE = "/./.";

    private static final Pattern BASEPATH_PATTERN = Pattern.compile(BASEPATH_SUBSTITUTE, Pattern.LITERAL);

    private String basePath;

    public void generate() throws IOException {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(this.source.getInputStream()));
            String line = null;
            while ((line = bf.readLine()) != null) {
                if (line.indexOf(BASEPATH_SUBSTITUTE) > -1) {
                    line = BASEPATH_PATTERN.matcher(line).replaceAll(this.basePath);
                }
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
        return this.source.getURI() + '?' + this.basePath;
    }

    @Override
    public long getLastModified() {
        return this.source.getLastModified();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }

    @Override
    protected void initialize() {
        this.basePath = ModelUtil.getString(this.model, Model.BASE_PATH);
    }
}
