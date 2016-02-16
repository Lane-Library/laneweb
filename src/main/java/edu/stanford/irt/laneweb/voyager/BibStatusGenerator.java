package edu.stanford.irt.laneweb.voyager;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class BibStatusGenerator extends AbstractGenerator implements ParametersAware {

    private static final String NS = "http://lane.stanford.edu/voyager/items/ns";

    private static final MessageFormat RECORD_URL = new MessageFormat("cocoon://apps/bib/{0}/status.xml");

    private Collection<String> bibIds = Collections.emptySet();

    private String bibList;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    public BibStatusGenerator(final SAXParser saxParser, final SourceResolver sourceResolver) {
        this.saxParser = saxParser;
        this.sourceResolver = sourceResolver;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        this.bibList = parameters.get("bids");
        if (this.bibList != null) {
            this.bibIds = new ArrayList<>();
            for (String b : this.bibList.split(",")) {
                this.bibIds.add(b);
            }
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, NS, "bibs");
            for (String bid : this.bibIds) {
                Object[] urlParams = { bid };
                String url = RECORD_URL.format(urlParams);
                Source source = this.sourceResolver.resolveURI(new URI(url));
                this.saxParser.parse(source, new EmbeddedXMLPipe(xmlConsumer));
            }
            XMLUtils.endElement(xmlConsumer, NS, "bibs");
            xmlConsumer.endDocument();
        } catch (SAXException | URISyntaxException e) {
            throw new LanewebException(e);
        }
    }
}
