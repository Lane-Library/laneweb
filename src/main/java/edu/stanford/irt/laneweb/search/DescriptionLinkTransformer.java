package edu.stanford.irt.laneweb.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.ImmutableEmptyAttributes;

public class DescriptionLinkTransformer extends AbstractTextProcessingTransformer {

    private static final Attributes EMPTY_ATTRIBUTES = new ImmutableEmptyAttributes();

    private static final Pattern LINK_PATTERN = Pattern.compile("\\[(.*)\\]\\((https?://[A-Za-z0-9\\./\\?=]+)\\)");

    @Override
    protected void createSAXEvents(final XMLConsumer consumer, final Matcher matcher) throws SAXException {
        consumer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION_LINK, Resource.DESCRIPTION_LINK,
                EMPTY_ATTRIBUTES);
        consumer.startElement(Resource.NAMESPACE, Resource.LABEL, Resource.LABEL, EMPTY_ATTRIBUTES);
        char[] match = matcher.group(1).toCharArray();
        consumer.characters(match, 0, match.length);
        consumer.endElement(Resource.NAMESPACE, Resource.LABEL, Resource.LABEL);
        consumer.startElement(Resource.NAMESPACE, Resource.LINK, Resource.LINK, EMPTY_ATTRIBUTES);
        match = matcher.group(2).toCharArray();
        consumer.characters(match, 0, match.length);
        consumer.endElement(Resource.NAMESPACE, Resource.LINK, Resource.LINK);
        consumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION_LINK, Resource.DESCRIPTION_LINK);
    }

    @Override
    protected Pattern getPattern() {
        return LINK_PATTERN;
    }

    @Override
    protected boolean isTargetName(final String name) {
        return "description".equals(name);
    }
}
