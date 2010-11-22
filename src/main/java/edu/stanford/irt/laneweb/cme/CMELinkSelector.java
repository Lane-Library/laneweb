package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.ModelUtil;


public class CMELinkSelector implements Selector {
    
    private static final String EMPTY_STRING = "";

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean select(String expression, Map objectModel, Parameters parameters) {
        return !EMPTY_STRING.equals(ModelUtil.getString(objectModel, Model.EMRID, EMPTY_STRING));
    }
}
