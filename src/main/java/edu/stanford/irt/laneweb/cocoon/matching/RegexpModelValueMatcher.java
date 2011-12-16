package edu.stanford.irt.laneweb.cocoon.matching;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.matching.AbstractRegexpMatcher;

public class RegexpModelValueMatcher extends AbstractRegexpMatcher {

    @Override
    protected String getMatchString(final Map objectModel, final Parameters parameters) {
        String paramName = parameters.getParameter("parameter-name", null);
        return objectModel.get(paramName).toString();
    }
}
