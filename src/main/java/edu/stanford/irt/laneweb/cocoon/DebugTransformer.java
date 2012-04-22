package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * This transformer puts a comment with all model attributes at the top of a
 * document.
 */
public class DebugTransformer extends AbstractTransformer implements ModelAware {

    private boolean debug;
    
    private Set<Entry<String, Object>> modelEntries;

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
    
    public void setModel(Map<String, Object> model) {
        this.debug = ModelUtil.getObject(model, Model.DEBUG, Boolean.class, Boolean.FALSE);
        this.modelEntries = model.entrySet();
    }
}
