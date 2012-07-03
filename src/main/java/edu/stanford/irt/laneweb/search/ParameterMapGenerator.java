package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class ParameterMapGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private Map<String, String[]> parameters;

    @SuppressWarnings("unchecked")
    public void setModel(final Map<String, Object> model) {
        this.parameters = new HashMap<String, String[]>();
        Map<String, String[]> map = ModelUtil.getObject(model, Model.PARAMETER_MAP, Map.class);
        for (Entry<String, String[]> entry : map.entrySet()) {
            this.parameters.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        marshall(this.parameters, xmlConsumer);
    }
}
