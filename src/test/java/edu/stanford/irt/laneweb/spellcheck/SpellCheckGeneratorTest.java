package edu.stanford.irt.laneweb.spellcheck;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

// $Id$
public class SpellCheckGeneratorTest {

    private SpellCheckGenerator generator;

    private Map<String, Object> model;

    private SpellChecker spellChecker;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.generator = new SpellCheckGenerator();
        this.spellChecker = createMock(SpellChecker.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testGenerate() throws IOException, SAXException {
        expect(this.spellChecker.spellCheck("ibuprophen")).andReturn(new SpellCheckResult("ibuprofen"));
        this.model.put(Model.QUERY, "ibuprophen");
        replayMocks();
        this.generator.setup(null, this.model, null, null);
        this.generator.setConsumer(this.xmlConsumer);
        this.generator.setSpellChecker(this.spellChecker);
        this.generator.generate();
        verifyMocks();
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
    public void testSetup() throws SAXException, IOException {
        this.model.put(Model.QUERY, "ibuprophen");
        replayMocks();
        this.generator.setup(null, this.model, null, null);
        verifyMocks();
    }

    @Test
    public void testSetupNullQuery() throws SAXException, IOException {
        replayMocks();
        try {
            this.generator.setup(null, this.model, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.spellChecker);
    }

    private void verifyMocks() {
        verify(this.spellChecker);
    }
}
