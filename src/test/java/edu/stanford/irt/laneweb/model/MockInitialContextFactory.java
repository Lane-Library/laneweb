package edu.stanford.irt.laneweb.model;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * Provides a JNDI initial context factory for the MockContext.
 * $Id$
 */
public class MockInitialContextFactory implements InitialContextFactory {

    private static Context MOCK_CONTEXT = null;

    public static void setMockContext(final Context ctx) {
        MOCK_CONTEXT = ctx;
    }

    public Context getInitialContext(final java.util.Hashtable<?, ?> environment) throws NamingException {
        if (MOCK_CONTEXT == null) {
            throw new IllegalStateException("mock context was not set.");
        }
        return MOCK_CONTEXT;
    }
}
