package edu.stanford.irt.laneweb.cocoon;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class ParameterMapGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private Map<String, Object> model;

    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Set<Entry<String, String[]>> entries = new HashSet<Entry<String, String[]>>();
        entries.addAll(ModelUtil.getObject(this.model, Model.PARAMETER_MAP, Map.class).entrySet());
        marshall(entries, xmlConsumer);
    }
}
