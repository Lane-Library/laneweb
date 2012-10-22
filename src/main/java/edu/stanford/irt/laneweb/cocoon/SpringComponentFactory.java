package edu.stanford.irt.laneweb.cocoon;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;

public class SpringComponentFactory implements ComponentFactory, BeanFactoryAware {

    private BeanFactory beanFactory;

    public Object getComponent(final String name) {
        return this.beanFactory.getBean(name);
    }

    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
