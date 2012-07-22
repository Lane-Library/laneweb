package edu.stanford.irt.laneweb.util;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class ImmutableEmptyAttributes extends AttributesImpl {

    @Override
    public void addAttribute(final String uri, final String localName, final String qName, final String type, final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final int index, final String uri, final String localName, final String qName, final String type,
            final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttributes(final Attributes atts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocalName(final int index, final String localName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQName(final int index, final String qName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setType(final int index, final String type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setURI(final int index, final String uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(final int index, final String value) {
        throw new UnsupportedOperationException();
    }
}
