package edu.stanford.irt.cocoon.pipeline.generate;

import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.laneweb.cocoon.AbstractSitemapModelComponent;

public abstract class AbstractGenerator extends AbstractSitemapModelComponent implements Generator {

    private XMLConsumer xmlConsumer;

    public void generate() {
        doGenerate(this.xmlConsumer);
    }

    public final void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }

    protected abstract void doGenerate(XMLConsumer xmlConsumer);
}
