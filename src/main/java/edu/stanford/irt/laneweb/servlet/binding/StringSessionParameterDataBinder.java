package edu.stanford.irt.laneweb.servlet.binding;

public class StringSessionParameterDataBinder extends SessionParameterDataBinder<String> {

    @Override
    protected String getParameterAsObject(final String parameterValue) {
        return parameterValue;
    }
}
