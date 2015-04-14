package edu.stanford.irt.laneweb.servlet.binding;

public class StringSessionParameterDataBinder extends SessionParameterDataBinder<String> {

    public StringSessionParameterDataBinder(final String modelKey, final String parameterName) {
        super(modelKey, parameterName);
    }

    @Override
    protected String getParameterAsObject(final String parameterValue) {
        return parameterValue;
    }
}
