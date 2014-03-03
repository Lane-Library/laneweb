package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

/**
 * A Generator that simply throws a LanewebException with a message.
 */
public class ExceptionGenerator extends AbstractGenerator implements ParametersAware {

    private String message;

    @Override
    public void setParameters(final Map<String, String> parameters) {
        this.message = parameters.get("message");
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        throw new LanewebException(this.message);
    }
}
