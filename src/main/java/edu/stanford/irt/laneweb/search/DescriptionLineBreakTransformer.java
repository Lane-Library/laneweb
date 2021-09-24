package edu.stanford.irt.laneweb.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.ImmutableEmptyAttributes;

public class DescriptionLineBreakTransformer extends AbstractTextProcessingTransformer {

    private static final Pattern BREAK_PATTERN = Pattern.compile("(<br ?/?>)", Pattern.CASE_INSENSITIVE);

    private static final Attributes EMPTY_ATTRIBUTES = new ImmutableEmptyAttributes();

    @Override
    protected void createSAXEvents(final XMLConsumer consumer, final Matcher matcher) throws SAXException {
        consumer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION_LINEBREAK, Resource.DESCRIPTION_LINEBREAK,
                EMPTY_ATTRIBUTES);
        char[] match = matcher.group(1).toCharArray();
        consumer.characters(match, 0, match.length);
        consumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION_LINEBREAK, Resource.DESCRIPTION_LINEBREAK);
    }

    @Override
    protected Pattern getPattern() {
        return BREAK_PATTERN;
    }

    @Override
    protected boolean isTargetName(final String name) {
        return "description".equals(name);
    }
}
