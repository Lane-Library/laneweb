package edu.stanford.irt.laneweb.popular;

import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class PopularListGenerator extends AbstractGenerator {

    private int limit;

    private String resourceType;

    private SAXStrategy<List<Map<String, String>>> saxStrategy;

    private BigqueryService service;

    public PopularListGenerator(final BigqueryService service,
            final SAXStrategy<List<Map<String, String>>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            this.resourceType = parameters.get(Model.TYPE);
        }
        if (parameters.containsKey(Model.LIMIT)) {
            String l = parameters.getOrDefault(Model.LIMIT, "10");
            try {
                this.limit = Integer.parseInt(l);
            } catch (NumberFormatException nfe) {
                this.limit = 0;
            }
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        List<Map<String, String>> resources = this.service.getPopularResources(this.resourceType);
        if (!resources.isEmpty() && resources.size() > this.limit) {
            resources = resources.subList(0, this.limit);
        }
        this.saxStrategy.toSAX(resources, xmlConsumer);
    }
}
