package edu.stanford.irt.laneweb.popular;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class PopularListGenerator extends AbstractGenerator {

    private static final long DEFAULT_EXPIRES = Duration.ofHours(1).toMillis();

    private String key;

    private int limit;

    private String resourceType;

    private SAXStrategy<List<Map<String, String>>> saxStrategy;

    private BigqueryService service;

    private Validity validity;

    public PopularListGenerator(final BigqueryService service,
            final SAXStrategy<List<Map<String, String>>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey().toString();
        }
        return this.key;
    }

    @Override
    public Validity getValidity() {
        if (this.validity == null) {
            this.validity = new ExpiresValidity(DEFAULT_EXPIRES);
        }
        return this.validity;
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

    private Serializable createKey() {
        return (new StringBuilder("t=").append(this.resourceType).append(";l=").append(this.limit)).toString();
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
