package edu.stanford.irt.laneweb.spellcheck;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

//TODO need to test for thread safety
public class SpellCheckGeneratorTest {
    
    private SpellCheckGenerator generator;
    
    private ServiceManager serviceManager;
    
    private SpellChecker spellChecker;
    
    private Parameters params;
    
    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.generator = new SpellCheckGenerator();
        this.serviceManager = createMock(ServiceManager.class);
        this.spellChecker = createMock(SpellChecker.class);
        this.params = createMock(Parameters.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDispose() {
        this.generator.dispose();
    }

    @Test
    public void testService() throws ServiceException {
        try {
            this.generator.service(null);
            fail();
        } catch (IllegalArgumentException e) {}
        expect(this.serviceManager.lookup(SpellChecker.class.getName())).andReturn(this.spellChecker);
        replay(this.serviceManager);
        this.generator.service(this.serviceManager);
        verify(this.serviceManager);
    }

    @Test
    public void testGenerate() throws ProcessingException, IOException, SAXException, ServiceException {
        expect(this.serviceManager.lookup(SpellChecker.class.getName())).andReturn(this.spellChecker);
        replay(this.serviceManager);
        expect(this.spellChecker.spellCheck("ibuprophen")).andReturn(new SpellCheckResult("ibuprofen"));
        replay(this.spellChecker);
        expect(this.params.getParameter("query",null)).andReturn("ibuprophen");
        replay(this.params);
        this.generator.service(this.serviceManager);
        this.generator.setConsumer(this.xmlConsumer);
        this.generator.setup(null, null, null, this.params);
        this.generator.generate();
        verify(this.serviceManager);
        verify(this.spellChecker);
        verify(this.params);
    }

    @Test
    public void testSetup() throws ProcessingException, SAXException, IOException {
        try {
            this.generator.setup(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {}
        expect(this.params.getParameter("query",null)).andReturn(null);
        expect(this.params.getParameter("query",null)).andReturn("ibuprophen");
        replay(this.params);
        try {
            this.generator.setup(null, null, null, this.params);
            fail();
        } catch (IllegalArgumentException e) {}
        this.generator.setup(null, null, null, this.params);
        verify(this.params);
    }

    @Test
    public void testSetConsumer() {
        try {
            this.generator.setConsumer(null);
            fail();
        } catch (IllegalArgumentException e) {}
        this.generator.setConsumer(this.xmlConsumer);
    }

}
