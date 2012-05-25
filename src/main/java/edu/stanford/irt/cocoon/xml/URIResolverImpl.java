package edu.stanford.irt.cocoon.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.excalibur.source.SourceResolver;

public class URIResolverImpl implements URIResolver {

    private SourceResolver sourceResolver;

    public URIResolverImpl(final SourceResolver sourceResolver) {
        this.sourceResolver = sourceResolver;
    }

    /**
     * Called by the processor when it encounters an xsl:include, xsl:import, or
     * document() function.
     * 
     * @param href
     *            An href attribute, which may be relative or absolute.
     * @param base
     *            The base URI in effect when the href attribute was
     *            encountered.
     * @return A Source object, or null if the href cannot be resolved, and the
     *         processor should try to resolve the URI itself.
     * @throws TransformerException
     *             if an error occurs when trying to resolve the URI.
     */
    public javax.xml.transform.Source resolve(final String href, final String base) throws TransformerException {
        org.apache.excalibur.source.Source source = null;
        try {
            if (base == null || href.indexOf(":") > 1) {
                // Null base - href must be an absolute URL
                source = this.sourceResolver.resolveURI(href);
            } else if (href.length() == 0) {
                // Empty href resolves to base
                source = this.sourceResolver.resolveURI(base);
            } else {
                // is the base a file or a real url
                if (!base.startsWith("file:")) {
                    int lastPathElementPos = base.lastIndexOf('/');
                    if (lastPathElementPos == -1) {
                        // this should never occur as the base should
                        // always be protocol:/....
                        return null; // we can't resolve this
                    } else {
                        source = this.sourceResolver.resolveURI(base.substring(0, lastPathElementPos) + "/" + href);
                    }
                } else {
                    File parent = new File(base.substring(5));
                    File parent2 = new File(parent.getParentFile(), href);
                    source = this.sourceResolver.resolveURI(parent2.toURI().toURL().toExternalForm());
                }
            }
            return new StreamSource(source.getInputStream(), source.getURI());
        } catch (IOException ioe) {
            return null;
        }
    }
}
