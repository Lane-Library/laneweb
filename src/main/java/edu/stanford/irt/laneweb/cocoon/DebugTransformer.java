package edu.stanford.irt.laneweb.cocoon;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * This transformer puts a comment with all model attributes at the top of a document.
 */
public class DebugTransformer extends AbstractXMLPipe implements Transformer {

    private Map<String, Object> model;

    private List<String> notToDisplay;
    
    
    public DebugTransformer(List<String> disallowList) {
        this.model = Collections.emptyMap();
        this.notToDisplay = disallowList;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        if (ModelUtil.getObject(this.model, Model.DEBUG, Boolean.class, Boolean.FALSE).booleanValue()) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Object> entry : this.model.entrySet()) {
                if( !this.notToDisplay.contains(entry.getKey())) {
                    sb.append('\n').append(entry.getKey()).append(": ").append(entry.getValue());
                }
            }
            sb.append('\n');
            comment(sb.toString().toCharArray(), 0, sb.length());
        }
    }
}
