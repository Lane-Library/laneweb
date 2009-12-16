package edu.stanford.irt.laneweb.icd9;

import java.lang.reflect.Method;

import org.apache.avalon.framework.parameters.Parameters;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import edu.stanford.irt.lane.icd9.ICD9Translator;

public class ICD9Advisor implements MethodBeforeAdvice, InitializingBean {

    ICD9Translator tranlastor;

    private static final long serialVersionUID = 1L;

    public void before(Method method, Object[] arg, Object claz)throws Throwable {
        Parameters params = (Parameters) arg[3];
        String    query = params.getParameter("query", null);
        if(query != null){
            if(tranlastor.isICD9Code(query))
                query = tranlastor.getLongName(query);    
            params.setParameter("query", query);
        }        
    }

    
    
    public void setTranslator(ICD9Translator translator) {
        this.tranlastor = translator;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.tranlastor,    "A translator is required. Use setTranslator(ICD9Translator) to provide one.");
    }

}
