package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.model.DefaultModelAware;
import edu.stanford.irt.laneweb.model.Model;


public class RequestSchemeSelector extends DefaultModelAware implements Selector {

    public boolean select(String expression, Map objectModel, Parameters parameters) {
        String scheme = this.model.getString(Model.REQUEST_SCHEME);
        return expression.equals(scheme);
    }
}
