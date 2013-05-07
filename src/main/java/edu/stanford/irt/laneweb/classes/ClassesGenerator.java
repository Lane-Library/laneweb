package edu.stanford.irt.laneweb.classes;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;

public class ClassesGenerator extends URLGenerator {

    public ClassesGenerator(final String type) {
        super(type);
    }

    @Override
    public Validity getValidity() {
        return new ClassesValidity();
    }
}
