package edu.stanford.irt.laneweb.classes;

import org.apache.excalibur.source.SourceValidity;

import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;

public class ClassesGenerator extends URLGenerator {

    public ClassesGenerator(final String type) {
        super(type);
    }

    @Override
    public SourceValidity getValidity() {
        return new ClassesValidity();
    }
}
