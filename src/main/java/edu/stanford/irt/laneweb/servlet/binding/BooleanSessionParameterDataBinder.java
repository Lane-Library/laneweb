package edu.stanford.irt.laneweb.servlet.binding;

public class BooleanSessionParameterDataBinder extends SessionParameterDataBinder<Boolean> {

    public BooleanSessionParameterDataBinder(final String modelKey, final String parameterName) {
        super(modelKey, parameterName);
    }

    @Override
    protected Boolean getParameterAsObject(final String parameterValue) {
        return Boolean.valueOf(parameterValue);
    }
}
