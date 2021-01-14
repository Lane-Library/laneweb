package edu.stanford.irt.laneweb.cocoon;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class TransformerErrorListener implements ErrorListener {

    private static final Logger log = LoggerFactory.getLogger(TransformerErrorListener.class);

    @Override
    public void error(final TransformerException te) {
        throw new LanewebException(getMessage(te), te);
    }

    @Override
    public void fatalError(final TransformerException te) {
        throw new LanewebException(getMessage(te), te);
    }

    @Override
    public void warning(final TransformerException te) {
        log.warn(getMessage(te));
    }

    private String getMessage(final TransformerException te) {
        StringBuilder sb = new StringBuilder(te.getMessage());
        SourceLocator locator = te.getLocator();
        if (locator != null) {
            sb.append(" publicId: ").append(locator.getPublicId()).append(" systemId: ").append(locator.getSystemId())
                    .append(" line: ").append(locator.getLineNumber()).append(" column: ")
                    .append(locator.getColumnNumber());
        }
        return sb.toString();
    }
}
