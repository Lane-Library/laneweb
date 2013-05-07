package edu.stanford.irt.laneweb.cocoon;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;

public class SpringComponentFactory implements ComponentFactory, BeanFactoryAware {

    private BeanFactory beanFactory;

    public <T> T getComponent(final String name, Class<T> requiredType) {
        return this.beanFactory.getBean(name, requiredType);
    }

    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
