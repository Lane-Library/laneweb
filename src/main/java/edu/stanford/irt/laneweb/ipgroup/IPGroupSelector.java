package edu.stanford.irt.laneweb.ipgroup;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.ModelUtil;

public class IPGroupSelector implements Selector {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean select(final String expression, final Map objectModel, final Parameters parameters) {
        IPGroup group = ModelUtil.getObject(objectModel, Model.IPGROUP, IPGroup.class);
        return expression.equals(group.toString());
    }
}
