package edu.stanford.irt.laneweb.classes;

import org.apache.excalibur.source.SourceValidity;

import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;

public class ClassesGenerator extends URLGenerator {

    @Override
    public SourceValidity getValidity() {
        return new ClassesValidity();
    }
}
