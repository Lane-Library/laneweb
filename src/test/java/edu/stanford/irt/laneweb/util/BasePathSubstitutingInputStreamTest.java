package edu.stanford.irt.laneweb.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;


public class BasePathSubstitutingInputStreamTest {
    
    private String substituted = "here is some text with /stage/ in it";
    
    private BasePathSubstitutingInputStream in;
    
    private InputStream original = new ByteArrayInputStream("here is some text with /././ in it".getBytes());

    @Before
    public void setUp() throws Exception {
        this.in = new BasePathSubstitutingInputStream(this.original, "/stage");
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = this.in.read()) != -1) {
            baos.write(i);
        }
        assertEquals(this.substituted, new String(baos.toByteArray()));
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[1024];
        int length = this.in.read(buffer);
        assertEquals(this.substituted, new String(buffer, 0, length));
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        byte[] buffer = new byte[1024];
        int length = this.in.read(buffer, 10, 27);
        assertEquals(this.substituted.substring(0, 27), new String(buffer, 10, length));
    }

    @Test
    public void testMarkSupported() {
        assertFalse(in.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] buffer = new byte[1024];
        long skip = 26;
        assertEquals(skip, this.in.skip(skip));
        int length = this.in.read(buffer);
        assertEquals("age/ in it", new String(buffer, 0, length));
    }
    
    @Test
    public void testSkipALot() throws IOException {
        byte[] buffer = new byte[1024];
        long skip = 7892342177l;
        assertEquals(this.substituted.length(), this.in.skip(skip));
        assertEquals(-1, this.in.read(buffer));
    }
}
