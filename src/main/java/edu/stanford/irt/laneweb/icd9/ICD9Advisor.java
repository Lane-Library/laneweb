package edu.stanford.irt.laneweb.icd9;

import java.lang.reflect.Method;

import org.apache.avalon.framework.parameters.Parameters;
import org.springframework.aop.MethodBeforeAdvice;

import edu.stanford.irt.lane.icd9.ICD9Translator;

public class ICD9Advisor implements MethodBeforeAdvice {

    private static final long serialVersionUID = 1L;

    private ICD9Translator tranlastor;

    public void before(final Method method, final Object[] arg, final Object claz) throws Throwable {
        Parameters params = (Parameters) arg[3];
        String query = params.getParameter("query", null);
        if (query != null) {
            if (this.tranlastor.isICD9Code(query)) {
                query = this.tranlastor.getLongName(query);
                params.setParameter("query", query);
            }
        }
    }

    public void setTranslator(final ICD9Translator translator) {
        this.tranlastor = translator;
    }
}
