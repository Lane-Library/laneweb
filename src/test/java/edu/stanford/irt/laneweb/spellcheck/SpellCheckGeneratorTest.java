package edu.stanford.irt.laneweb.spellcheck;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

//$Id$
public class SpellCheckGeneratorTest {

    private SpellCheckGenerator generator;

    private Model model;

    private SpellChecker spellChecker;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.generator = new SpellCheckGenerator();
        this.spellChecker = createMock(SpellChecker.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.model = createMock(Model.class);
        this.generator.setModel(this.model);
    }

    @Test
    public void testGenerate() throws ProcessingException, IOException, SAXException {
        expect(this.spellChecker.spellCheck("ibuprophen")).andReturn(new SpellCheckResult("ibuprofen"));
        expect(this.model.getString(LanewebObjectModel.QUERY)).andReturn("ibuprophen");
        replayMocks();
        this.generator.setup(null, null, null, null);
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
    public void testSetup() throws ProcessingException, SAXException, IOException {
        expect(this.model.getString(LanewebObjectModel.QUERY)).andReturn("ibuprophen");
        replayMocks();
        this.generator.setup(null, null, null, null);
        verifyMocks();
    }

    @Test
    public void testSetupNullQuery() throws ProcessingException, SAXException, IOException {
        expect(this.model.getString(LanewebObjectModel.QUERY)).andReturn(null);
        replayMocks();
        try {
            this.generator.setup(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.spellChecker);
        replay(this.model);
    }

    private void verifyMocks() {
        verify(this.spellChecker);
        verify(this.model);
    }
}
