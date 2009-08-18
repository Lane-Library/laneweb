package edu.stanford.irt.laneweb.spellcheck;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

public class SpellCheckGeneratorTest {

    private SpellCheckGenerator generator;

    private Parameters params;

    private SpellChecker spellChecker;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.generator = new SpellCheckGenerator();
        this.spellChecker = createMock(SpellChecker.class);
        this.params = createMock(Parameters.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testGenerate() throws ProcessingException, IOException, SAXException {
        expect(this.spellChecker.spellCheck("ibuprophen")).andReturn(new SpellCheckResult("ibuprofen"));
        replay(this.spellChecker);
        expect(this.params.getParameter("query", null)).andReturn("ibuprophen");
        replay(this.params);
        this.generator.setup(null, null, null, this.params);
        this.generator.setConsumer(this.xmlConsumer);
        this.generator.setSpellChecker(this.spellChecker);
        this.generator.generate();
        verify(this.spellChecker);
        verify(this.params);
    }

    @Test
    public void testSetConsumer() {
        try {
            this.generator.setConsumer(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.generator.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testSetSpellCheck() {
        try {
            this.generator.setSpellChecker(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.generator.setSpellChecker(this.spellChecker);
    }

    @Test
    public void testSetup() throws ProcessingException, SAXException, IOException {
        try {
            this.generator.setup(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.params.getParameter("query", null)).andReturn(null);
        expect(this.params.getParameter("query", null)).andReturn("ibuprophen");
        replay(this.params);
        try {
            this.generator.setup(null, null, null, this.params);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.generator.setup(null, null, null, this.params);
        verify(this.params);
    }
}
