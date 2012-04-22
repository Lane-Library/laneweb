package edu.stanford.irt.laneweb.cocoon;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;


public class ParameterMapGenerator extends AbstractMarshallingGenerator implements ModelAware {
    
    private Map<String, Object> model;

    @SuppressWarnings("unchecked")
    public void generate() {
        Set<Entry<String, String[]>> entries = new HashSet<Entry<String, String[]>>();
        entries.addAll(ModelUtil.getObject(this.model, Model.PARAMETER_MAP, Map.class).entrySet());
        marshall(entries);
    }
    
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
     
}
