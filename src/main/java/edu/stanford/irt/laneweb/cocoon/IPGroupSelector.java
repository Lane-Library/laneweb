package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.DefaultModelAware;
import edu.stanford.irt.laneweb.model.Model;


public class IPGroupSelector extends DefaultModelAware implements Selector {

    public boolean select(String expression, Map objectModel, Parameters parameters) {
        IPGroup group = this.model.getObject(Model.IPGROUP, IPGroup.class);
        return expression.equals(group.toString());
    }
}
