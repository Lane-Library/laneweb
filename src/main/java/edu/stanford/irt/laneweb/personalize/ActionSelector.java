package edu.stanford.irt.laneweb.personalize;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;


public class ActionSelector implements Selector {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean select(String expression, Map objectModel, Parameters parameters) {
        return expression.equals(ModelUtil.getString(objectModel, Model.ACTION));
    }
}
