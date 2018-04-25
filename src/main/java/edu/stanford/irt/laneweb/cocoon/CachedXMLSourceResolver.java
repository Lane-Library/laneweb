package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;

import javax.cache.Cache;

import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLByteStreamCompiler;
import edu.stanford.irt.cocoon.xml.XMLByteStreamInterpreter;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.cocoon.xml.XMLException;
import edu.stanford.irt.cocoon.xml.XMLizable;

public class CachedXMLSourceResolver extends CacheSourceResolver {

    private static final class CachedXMLSource extends ByteArraySource implements XMLizable {

        private byte[] bytesArray;

        CachedXMLSource(final byte[] byteArray, final String uri, final Validity validity) {
            super(byteArray, uri, validity);
            this.bytesArray = byteArray;
        }

        @Override
        public void toSAX(final XMLConsumer handler) {
            XMLByteStreamInterpreter interpreter = new XMLByteStreamInterpreter();
            interpreter.setXMLConsumer(handler);
            interpreter.deserialize(this.bytesArray);
        }
    }

    private SAXParser parser;

    public CachedXMLSourceResolver(final SAXParser parser, final Cache<Serializable, CachedResponse> cache,
            final SourceResolver sourceResolver) {
        super(cache, sourceResolver);
        this.parser = parser;
    }

    @Override
    protected Source createSource(final byte[] bytes, final String uri, final Validity validity) {
        return new CachedXMLSource(bytes, uri, validity);
    }

    @Override
    protected byte[] getBytesFromSource(final Source source) {
        XMLByteStreamCompiler compiler = new XMLByteStreamCompiler();
        try {
            this.parser.parse(source, compiler);
        } catch (XMLException e) {
            String message = String.format("failed to get bytes from {}", source.getURI());
            throw new CacheSourceException(message, e);
        }
        return compiler.getBytes();
    }
}
