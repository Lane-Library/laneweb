package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.sax.SAXResult;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkGeneratorTest {

    private BookmarkGenerator generator;

    private Marshaller marshaller;

    @Before
    public void setUp() throws Exception {
        this.generator = new BookmarkGenerator();
        this.marshaller = createMock(Marshaller.class);
        this.generator.setMarshaller(this.marshaller);
        this.generator.setup(null, Collections.singletonMap(Model.BOOKMARKS, Collections.emptyList()), null, null);
    }

    @Test
    public void testGenerate() throws XmlMappingException, IOException {
        this.marshaller.marshal(isA(List.class), isA(SAXResult.class));
        replay(this.marshaller);
        this.generator.generate();
        verify(this.marshaller);
    }
    // @Test
    // public void testInitialize() {
    // this.generator.initialize();
    // }
}
