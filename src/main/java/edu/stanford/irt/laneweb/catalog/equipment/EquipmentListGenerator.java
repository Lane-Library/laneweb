package edu.stanford.irt.laneweb.catalog.equipment;

import java.util.List;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class EquipmentListGenerator extends AbstractGenerator {

    private SAXStrategy<List<Equipment>> saxStrategy;

    private EquipmentService service;

    public EquipmentListGenerator(final EquipmentService service, final SAXStrategy<List<Equipment>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(this.service.getList(), xmlConsumer);
    }
}
