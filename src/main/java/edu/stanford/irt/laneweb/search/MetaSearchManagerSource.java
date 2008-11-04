package edu.stanford.irt.laneweb.search;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private MetaSearchManager manager;

    private ClassPathXmlApplicationContext context;

    public MetaSearchManagerSource() {
        this.context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
        this.manager = (MetaSearchManager) this.context.getBean("manager");
    }

    public MetaSearchManager getMetaSearchManager() {
        return this.manager;
    }

    public void dispose() {
        this.context.destroy();
    }
}