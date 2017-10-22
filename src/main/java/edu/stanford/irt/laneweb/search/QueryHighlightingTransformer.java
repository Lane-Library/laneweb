package edu.stanford.irt.laneweb.search;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.ImmutableEmptyAttributes;

public class QueryHighlightingTransformer extends AbstractTextProcessingTransformer implements ModelAware {

    private static final Attributes EMPTY_ATTRIBUTES = new ImmutableEmptyAttributes();

    private static final Pattern UNMATCHABLE = Pattern.compile("$a");

    private Pattern queryPattern;

    public QueryHighlightingTransformer() {
        this.queryPattern = UNMATCHABLE;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        String query = ModelUtil.getString(model, Model.QUERY);
        if (query != null) {
            this.queryPattern = QueryTermPattern.getPattern(query);
        }
    }

    @Override
    protected void createSAXEvents(final XMLConsumer consumer, final Matcher matcher) throws SAXException {
        consumer.startElement(Resource.NAMESPACE, Resource.KEYWORD, Resource.KEYWORD, EMPTY_ATTRIBUTES);
        char[] match = matcher.group().toCharArray();
        consumer.characters(match, 0, match.length);
        consumer.endElement(Resource.NAMESPACE, Resource.KEYWORD, Resource.KEYWORD);
    }

    @Override
    protected Pattern getPattern() {
        return this.queryPattern;
    }

    @Override
    protected boolean isTargetName(final String name) {
        return "title".equals(name) || "description".equals(name);
    }
}
