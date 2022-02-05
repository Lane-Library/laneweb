 package edu.stanford.irt.laneweb.staff;

import java.io.Serializable;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class StaffGenerator extends AbstractGenerator {

    private SAXStrategy<Source> strategy;

    private Source source;

    public StaffGenerator(SAXStrategy<Source> strategy) {
        this.strategy = strategy;
    }

    @Override
    protected void doGenerate(XMLConsumer xmlConsumer) {
        this.strategy.toSAX(this.source, xmlConsumer);
    }

    @Override
    public Serializable getKey() {
        return this.source.getURI();
    }

    @Override
    public void setSource(final Source source) {
        this.source = source;
    }
}
