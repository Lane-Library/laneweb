package edu.stanford.irt.laneweb.icd9;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.lane.icd9.ICD9Translator;

public class ICD9JSONReaderTest {

    private ByteArrayOutputStream outputStream;

    private Parameters params;

    private ICD9JSONReader reader;

    private ICD9Translator translator;

    @Before
    public void setUp() throws Exception {
        this.reader = new ICD9JSONReader();
        this.translator = createMock(ICD9Translator.class);
        this.reader.setICD9Translator(this.translator);
        this.outputStream = new ByteArrayOutputStream();
        this.reader.setOutputStream(this.outputStream);
        this.params = createMock(Parameters.class);
    }

    @Test
    public void testGenerate() throws IOException {
        expect(this.params.getParameter("icd9", null)).andReturn("804");
        replay(this.params);
        expect(this.translator.getLongName("804")).andReturn("FOO");
        replay(this.translator);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"code\":\"804\",\"longName\":\"FOO\"}", new String(this.outputStream.toByteArray()));
        verify(this.params);
        verify(this.translator);
    }

    @Test
    public void testGenerateError() throws IOException {
        expect(this.params.getParameter("icd9", null)).andReturn("foo");
        replay(this.params);
        expect(this.translator.getLongName("foo")).andThrow(new IllegalArgumentException("FOO"));
        replay(this.translator);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"code\":\"foo\",\"error\":\"FOO\"}", new String(this.outputStream.toByteArray()));
        verify(this.params);
        verify(this.translator);
    }

    @Test
    public void testGenerateEscapeQuote() throws IOException {
        expect(this.params.getParameter("icd9", null)).andReturn("fo\"o");
        replay(this.params);
        expect(this.translator.getLongName("fo\"o")).andThrow(new IllegalArgumentException("FOO"));
        replay(this.translator);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"code\":\"fo\\\"o\",\"error\":\"FOO\"}", new String(this.outputStream.toByteArray()));
        verify(this.params);
        verify(this.translator);
    }
}
