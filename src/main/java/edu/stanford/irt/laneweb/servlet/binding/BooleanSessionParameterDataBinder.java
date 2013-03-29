package edu.stanford.irt.laneweb.servlet.binding;

public class BooleanSessionParameterDataBinder extends SessionParameterDataBinder<Boolean> {

    @Override
    protected Boolean getParameterAsObject(final String parameterValue) {
        return Boolean.parseBoolean(parameterValue);
    }
}
