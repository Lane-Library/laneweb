package edu.stanford.irt.laneweb.servlet;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * Provides a JNDI initial context factory for the MockContext.
 */
public class MockInitialContextFactory implements InitialContextFactory {

    private static Context mockContext = null;

    public static void setMockContext(final Context ctx) {
        mockContext = ctx;
    }

    @Override
    public Context getInitialContext(final java.util.Hashtable<?, ?> environment) throws NamingException {
        if (mockContext == null) {
            throw new IllegalStateException("mock context was not set.");
        }
        return mockContext;
    }
}
