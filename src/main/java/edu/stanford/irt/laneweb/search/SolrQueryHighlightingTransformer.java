package edu.stanford.irt.laneweb.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.ImmutableEmptyAttributes;

/**
 * A {@code Transformer} to highlight query terms found in title and description
 * fields. Relies on Solr for query highlighting. ___ and ::: are start/end tags
 * as defined in
 * edu.stanford.irt.eresource.repository.solr.SolrRepository.HighlightTags
 * 
 */
public class SolrQueryHighlightingTransformer extends AbstractTextProcessingTransformer {

    private static final Attributes EMPTY_ATTRIBUTES = new ImmutableEmptyAttributes();

    private static final Pattern SOLR_HIGHLIGHT_PATTERN = Pattern.compile("___((?:(?!___).)+):::");

    @Override
    protected void createSAXEvents(final XMLConsumer consumer, final Matcher matcher) throws SAXException {
        consumer.startElement(Resource.NAMESPACE, Resource.KEYWORD, Resource.KEYWORD, EMPTY_ATTRIBUTES);
        char[] match = matcher.group(1).toCharArray();
        consumer.characters(match, 0, match.length);
        consumer.endElement(Resource.NAMESPACE, Resource.KEYWORD, Resource.KEYWORD);
    }

    @Override
    protected Pattern getPattern() {
        return SOLR_HIGHLIGHT_PATTERN;
    }

    @Override
    protected boolean isTargetName(final String name) {
        return Resource.TITLE.equals(name) || Resource.DESCRIPTION.equals(name) || Resource.AUTHOR.equals(name)
                || Resource.PUBLICATION_TEXT.equals(name);
    }
}
