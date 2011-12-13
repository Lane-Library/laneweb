package edu.stanford.irt.laneweb.personalize;

import static org.junit.Assert.fail;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.sax.SAXResult;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.laneweb.model.Model;

public class HistoryGeneratorTest {
    
    private HistoryGenerator generator;
    
    private Marshaller marshaller;

    @Before
    public void setUp() throws Exception {
        this.generator = new HistoryGenerator();
        this.marshaller = createMock(Marshaller.class);
        this.generator.setMarshaller(this.marshaller);
        this.generator.setup(null, Collections.singletonMap(Model.HISTORY, Collections.emptyList()), null, null);
    }

    @Test
    public void testGenerate() throws XmlMappingException, IOException {
        this.marshaller.marshal(isA(List.class), isA(SAXResult.class));
        replay(this.marshaller);
        this.generator.generate();
        verify(this.marshaller);
    }

    @Test
    public void testInitialize() {
        this.generator.initialize();
    }
}
