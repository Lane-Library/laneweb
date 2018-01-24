package edu.stanford.irt.laneweb.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * The presence of this class causes the Filter that enables redis sessions to be created
 */
public class SessionFilterInitializer extends AbstractHttpSessionApplicationInitializer {
}
