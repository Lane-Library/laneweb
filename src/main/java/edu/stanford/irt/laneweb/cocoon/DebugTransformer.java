package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.SAXException;

/**
 * This transformer puts a comment with all model attributes at the top of a document.
 * @author ceyates
 * 
 * $Id$
 *
 */
public class DebugTransformer extends AbstractTransformer {
    
    private boolean debug;

    @Override
    protected void initialize() {
        this.debug = this.model.getObject("debug", Boolean.class, Boolean.FALSE);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        if (this.debug) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Object> entry: ((Map<String, Object>)this.model).entrySet()) {
                sb.append('\n').append(entry.getKey()).append(": ").append(entry.getValue());
            }
            sb.append('\n');
            comment(sb.toString().toCharArray(), 0, sb.length());
        }
    }
    
    
}
