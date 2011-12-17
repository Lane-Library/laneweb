package edu.stanford.irt.laneweb.cocoon.matching;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.matching.AbstractRegexpMatcher;

import edu.stanford.irt.laneweb.LanewebException;

public class RegexpModelValueMatcher extends AbstractRegexpMatcher {

    @Override
    protected String getMatchString(final Map objectModel, final Parameters parameters) {
        String paramName = parameters.getParameter("parameter-name", null);
        if (paramName == null) {
            throw new LanewebException("null parameter-name");
        }
        Object obj = objectModel.get(paramName);
        return obj == null ? null : obj.toString();
    }
}
