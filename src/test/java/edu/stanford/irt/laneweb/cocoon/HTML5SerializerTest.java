package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import javax.xml.transform.Result;
import javax.xml.transform.sax.TransformerHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class HTML5SerializerTest {

    private HTML5Serializer serializer;

    private TransformerHandler transformerHandler;

    @BeforeEach
    public void setUp() {
        this.transformerHandler = mock(TransformerHandler.class);
        this.serializer = new HTML5Serializer(null, this.transformerHandler, Collections.emptyMap());
    }

    @Test
    public void testStartDocument() throws SAXException {
        this.transformerHandler.setResult(isA(Result.class));
        this.transformerHandler.startDocument();
        replay(this.transformerHandler);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.serializer.setOutputStream(baos);
        this.serializer.startDocument();
        assertArrayEquals("<!DOCTYPE html>\n".getBytes(), baos.toByteArray());
        verify(this.transformerHandler);
    }

    @Test
    public void testStartDocumentThrowsIOException() throws SAXException, IOException {
        assertThrows(SAXException.class, () -> {
            this.transformerHandler.setResult(isA(Result.class));
            OutputStream output = mock(OutputStream.class);
            output.write(isA(byte[].class));
            expectLastCall().andThrow(new IOException());
            replay(this.transformerHandler, output);
            this.serializer.setOutputStream(output);
            this.serializer.startDocument();
        });
    }
}
