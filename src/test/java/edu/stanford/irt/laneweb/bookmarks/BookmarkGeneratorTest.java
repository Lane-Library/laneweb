package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.sax.SAXResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkGeneratorTest {

    private BookmarkGenerator generator;

    private Marshaller marshaller;

    @BeforeEach
    public void setUp() throws Exception {
        this.marshaller = mock(Marshaller.class);
        this.generator = new BookmarkGenerator(this.marshaller);
        this.generator.setModel(Collections.singletonMap(Model.BOOKMARKS, Collections.emptyList()));
    }

    @Test
    public void testGenerate() throws XmlMappingException, IOException, SAXException {
        this.marshaller.marshal(isA(List.class), isA(SAXResult.class));
        replay(this.marshaller);
        this.generator.generate();
        verify(this.marshaller);
    }
}
