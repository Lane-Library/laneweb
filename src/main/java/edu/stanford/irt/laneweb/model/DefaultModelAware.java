package edu.stanford.irt.laneweb.model;

/**
 * Default implementation of {@link:ModelAware}.  Extending this allows a bean
 * to get values of various type, not just String and primitives as from Parameters.
 * 
 * @author ceyates
 *
 * $Id$
 */
public class DefaultModelAware implements ModelAware {
    
    protected Model model;

    public void setModel(Model model) {
        this.model = model;
    }
}
