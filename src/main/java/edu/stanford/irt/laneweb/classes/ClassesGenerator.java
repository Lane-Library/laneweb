package edu.stanford.irt.laneweb.classes;

import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;
import edu.stanford.irt.cocoon.source.SourceValidity;

public class ClassesGenerator extends URLGenerator {

    public ClassesGenerator(final String type) {
        super(type);
    }

    @Override
    public SourceValidity getValidity() {
        return new ClassesValidity();
    }
}
