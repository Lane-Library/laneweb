package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class GrandRoundsGenerator extends AbstractGenerator implements ParametersAware {

    private static final String DEPARTMENT = "department";

    private static final String YEAR = "year";

    private String department;

    private SAXStrategy<Presentation> presentationSAXStrategy;

    private GrandRoundsService service;

    private String year;

    public GrandRoundsGenerator(final GrandRoundsService service,
            final SAXStrategy<Presentation> presentationSAXStrategy) {
        this.service = service;
        this.presentationSAXStrategy = presentationSAXStrategy;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        this.department = parameters.get(DEPARTMENT);
        this.year = parameters.get(YEAR);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        List<Presentation> presentations = this.service.getGrandRounds(this.department, this.year);
        try {
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, "", "grandrounds");
            presentations.stream().forEach((final Presentation p)
                    -> this.presentationSAXStrategy.toSAX(p, xmlConsumer));
            XMLUtils.endElement(xmlConsumer, "", "grandrounds");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
