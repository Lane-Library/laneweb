package edu.stanford.irt.laneweb.classes;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;
import edu.stanford.irt.cocoon.xml.SAXParser;

public class ClassesGenerator extends URLGenerator {

    public ClassesGenerator(final String type, final SAXParser saxParser) {
        super(type, saxParser);
    }

    @Override
    public Validity getValidity() {
        return new ClassesValidity();
    }
}
