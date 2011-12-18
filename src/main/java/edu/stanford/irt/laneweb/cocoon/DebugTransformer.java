package edu.stanford.irt.laneweb.cocoon;

import java.util.Map.Entry;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * This transformer puts a comment with all model attributes at the top of a
 * document.
 */
public class DebugTransformer extends AbstractTransformer {

    private boolean debug;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        if (this.debug) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Object> entry : getModel().entrySet()) {
                sb.append('\n').append(entry.getKey()).append(": ").append(entry.getValue());
            }
            sb.append('\n');
            comment(sb.toString().toCharArray(), 0, sb.length());
        }
    }

    @Override
    protected void initialize() {
        this.debug = ModelUtil.getObject(getModel(), Model.DEBUG, Boolean.class, Boolean.FALSE);
    }
}
