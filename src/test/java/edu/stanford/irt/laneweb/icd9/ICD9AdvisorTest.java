package edu.stanford.irt.laneweb.icd9;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.lane.icd9.ICD9Translator;
import edu.stanford.irt.laneweb.model.Model;

public class ICD9AdvisorTest {

    ICD9Advisor advivor;

    ICD9Translator tranlastor;

    @Test
    public void beforeICD9TermTest() throws Throwable {
        String queryTerm = "050";
        Parameters parms = new Parameters();
        parms.setParameter(Model.QUERY, queryTerm);
        Object[] ob = new Object[4];
        ob[3] = parms;
        this.advivor.before(null, ob, null);
        assertEquals("SMALLPOX", ((Parameters) ob[3]).getParameter(Model.QUERY));
    }

    @Test
    public void beforeTest() throws Throwable {
        String queryTerm = "test";
        Parameters parms = new Parameters();
        parms.setParameter(Model.QUERY, queryTerm);
        Object[] ob = new Object[4];
        ob[3] = parms;
        this.advivor.before(null, ob, null);
        assertEquals(queryTerm, ((Parameters) ob[3]).getParameter(Model.QUERY));
    }

    @Before
    public void setUp() throws IOException {
        this.tranlastor = new ICD9Translator();
        this.advivor = new ICD9Advisor();
        this.advivor.setTranslator(this.tranlastor);
    }
}
