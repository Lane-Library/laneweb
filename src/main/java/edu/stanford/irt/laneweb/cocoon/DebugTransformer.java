package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * This transformer puts a comment with all model attributes at the top of a
 * document.
 */
public class DebugTransformer extends AbstractXMLPipe implements Transformer, ModelAware {

    private boolean debug;

    private Set<Entry<String, Object>> modelEntries;

    public void setModel(final Map<String, Object> model) {
        this.debug = ModelUtil.getObject(model, Model.DEBUG, Boolean.class, Boolean.FALSE);
        this.modelEntries = model.entrySet();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        if (this.debug) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Object> entry : this.modelEntries) {
                sb.append('\n').append(entry.getKey()).append(": ").append(entry.getValue());
            }
            sb.append('\n');
            comment(sb.toString().toCharArray(), 0, sb.length());
        }
    }
}
