package edu.stanford.irt.laneweb.model;

/**
 * Implementing this gives SitemapComponent beans access to the Model. This allows them to get values of various type,
 * not just String and primitives as from Parameters.
 * 
 * @author ceyates $Id$
 */
public interface ModelAware {

    void setModel(Model model);
}
