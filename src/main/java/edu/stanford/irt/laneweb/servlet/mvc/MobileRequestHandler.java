package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This subclass of ModelAugmentingRequestHandler doesn't put stuff that can change
 * while roaming into the session.
 * 
 * @author ceyates
 *
 */
public abstract class MobileRequestHandler extends ModelAugmentingRequestHandler {
}
