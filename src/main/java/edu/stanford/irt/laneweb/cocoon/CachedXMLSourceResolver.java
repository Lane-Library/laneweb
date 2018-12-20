package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;

import javax.cache.Cache;

import edu.stanford.irt.cocoon.CocoonException;
import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLByteStreamCompiler;
import edu.stanford.irt.cocoon.xml.XMLByteStreamInterpreter;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.cocoon.xml.XMLizable;

public class CachedXMLSourceResolver extends CacheSourceResolver {

    private static final class CachedXMLSource extends ByteArraySource implements XMLizable {

        private byte[] bytesArray;

        private XMLByteStreamInterpreter xmlByteStreamInterpreter;

        CachedXMLSource(final byte[] byteArray, final String uri, final Validity validity,
                final XMLByteStreamInterpreter xmlByteStreamInterpreter) {
            super(byteArray, uri, validity);
            this.bytesArray = byteArray;
        }

        @Override
        public void toSAX(final XMLConsumer handler) {
            this.xmlByteStreamInterpreter.deserialize(this.bytesArray, handler);
        }
    }

    private SAXParser parser;

    private XMLByteStreamInterpreter xmlByteStreamInterpreter;

    public CachedXMLSourceResolver(final SAXParser parser, final Cache<Serializable, CachedResponse> cache,
            final SourceResolver sourceResolver, final XMLByteStreamInterpreter xmlByteStreamInterpreter) {
        super(cache, sourceResolver);
        this.parser = parser;
        this.xmlByteStreamInterpreter = xmlByteStreamInterpreter;
    }

    @Override
    protected Source createSource(final byte[] bytes, final String uri, final Validity validity) {
        return new CachedXMLSource(bytes, uri, validity, this.xmlByteStreamInterpreter);
    }

    @Override
    protected byte[] getBytesFromSource(final Source source) {
        XMLByteStreamCompiler compiler = new XMLByteStreamCompiler();
        try {
            this.parser.parse(source, compiler);
        } catch (CocoonException e) {
            String message = String.format("failed to get bytes from %s", source.getURI());
            throw new CacheSourceException(message, e);
        }
        return compiler.getBytes();
    }
}
