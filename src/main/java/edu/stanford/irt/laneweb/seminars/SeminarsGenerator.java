package edu.stanford.irt.laneweb.seminars;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SeminarsGenerator extends AbstractGenerator {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final String SEMINARS_NS = "http://lane.stanford.edu/seminars/ns";

    private static final MessageFormat URL_FORMAT = new MessageFormat(
            "http://med.stanford.edu/seminars/validatecmecalendar.do?filter=true&selMonth={0}&selDay={1}&selYear={2}&futureNumberDays=60&departmentId=0&seminarLocation=0&keyword=&courseType=gran");

    private final DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("dd");

    private final DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM");

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    private final DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");

    public SeminarsGenerator(final SAXParser saxParser, final SourceResolver sourceResolver) {
        this.saxParser = saxParser;
        this.sourceResolver = sourceResolver;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        LocalDate today = LocalDate.now(AMERICA_LA);
        String day = this.dayFormat.format(today);
        String month = this.monthFormat.format(today);
        String year = this.yearFormat.format(today);
        Object[] urlParams = { month, day, year };
        String url = URL_FORMAT.format(urlParams);
        try {
            Source source = this.sourceResolver.resolveURI(new URI(url));
            xmlConsumer.startDocument();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(SEMINARS_NS, "url", "url", "CDATA", url);
            XMLUtils.startElement(xmlConsumer, SEMINARS_NS, "seminars", atts);
            this.saxParser.parse(source, new EmbeddedXMLPipe(xmlConsumer));
            XMLUtils.endElement(xmlConsumer, SEMINARS_NS, "seminars");
            xmlConsumer.endDocument();
        } catch (SAXException | URISyntaxException e) {
            throw new LanewebException(e);
        }
    }
}
