package edu.stanford.irt.laneweb.util;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;

import org.springframework.beans.factory.FactoryBean;


public class SAXTransformerFactoryBean implements FactoryBean<SAXTransformerFactory> {

    private SAXTransformerFactory factory;

    public SAXTransformerFactory getObject() throws Exception {
        return this.factory;
    }

    public Class<?> getObjectType() {
        return SAXTransformerFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }
    
    public void setTransformerFactoryClass(String factoryClass) {
        this.factory = (SAXTransformerFactory) TransformerFactory.newInstance(factoryClass, null);
    }
}
