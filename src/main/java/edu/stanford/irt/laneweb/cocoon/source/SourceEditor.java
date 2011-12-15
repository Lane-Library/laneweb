package edu.stanford.irt.laneweb.cocoon.source;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;

public class SourceEditor extends ResourceEditor {

    public SourceEditor(final ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void setAsText(final String text) {
        super.setAsText(text);
        setValue(new SpringResourceSource((Resource) getValue()));
    }
}
