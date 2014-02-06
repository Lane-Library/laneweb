package edu.stanford.irt.laneweb.seminars;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SeminarsGenerator extends AbstractGenerator implements ParametersAware {

    private static final String SEMINARS_NS = "http://lane.stanford.edu/seminars/ns";

    private static final MessageFormat URL_FORMAT = new MessageFormat(
            "http://med.stanford.edu/seminars/validatecmecalendar.do?filter=true&selMonth={0}&selDay={1}&selYear={2}&futureNumberDays=60&departmentId=0&seminarLocation=0&keyword=&courseType={3}");

    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

    private final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");

    private SAXParser saxParser;

    private Source source;

    private SourceResolver sourceResolver;

    private String type;

    private String url;

    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    public SeminarsGenerator(final SAXParser saxParser, final SourceResolver sourceResolver) {
        this.saxParser = saxParser;
        this.sourceResolver = sourceResolver;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        this.type = parameters.get(Model.TYPE);
        Date today = new Date();
        String day;
        String month;
        String year;
        synchronized (this) {
            day = this.dayFormat.format(today);
            month = this.monthFormat.format(today);
            year = this.yearFormat.format(today);
        }
        Object[] urlParams = { month, day, year, this.type };
        this.url = URL_FORMAT.format(urlParams);
        try {
            this.source = this.sourceResolver.resolveURI(new URI(this.url));
        } catch (URISyntaxException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(SEMINARS_NS, "type", "type", "CDATA", this.type);
            atts.addAttribute(SEMINARS_NS, "url", "url", "CDATA", this.url);
            XMLUtils.startElement(xmlConsumer, SEMINARS_NS, "seminars", atts);
            this.saxParser.parse(this.source, new EmbeddedXMLPipe(xmlConsumer));
            XMLUtils.endElement(xmlConsumer, SEMINARS_NS, "seminars");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
