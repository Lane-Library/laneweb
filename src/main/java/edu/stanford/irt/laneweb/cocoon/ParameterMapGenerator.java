package edu.stanford.irt.laneweb.cocoon;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;


public class ParameterMapGenerator extends AbstractMarshallingGenerator {

    @SuppressWarnings("unchecked")
    public void generate() {
        Set<Entry<String, String[]>> entries = new HashSet<Entry<String, String[]>>();
        entries.addAll(ModelUtil.getObject(getModel(), Model.PARAMETER_MAP, Map.class).entrySet());
        marshall(entries);
    }
     
}
