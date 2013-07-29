/**
 * 
 */
package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author ryanmax
 */
public class SHCCodecTest {

    private String ciphertext = "9muniFwV90BgjxJpnNC2pE/AY8pWD5iajvRw2uCTOfg=";

    private SHCCodec codec;

    private Logger log;

    private String plaintext = "plain piece of text";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.log = createMock(Logger.class);
        // note: not the real key and vector:
        this.codec = new SHCCodec("yfGIl68aDih3DamkzIJeYA==", "ABCDEFGHIJKLMNOP", this.log);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.servlet.SHCCodec#decrypt(java.lang.String)} .
     */
    @Test
    public final void testDecrypt() {
        assertEquals(this.plaintext, this.codec.decrypt(this.ciphertext));
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.servlet.SHCCodec#encrypt(java.lang.String)} .
     */
    @Test
    public final void testEncrypt() {
        assertEquals(this.ciphertext, this.codec.encrypt(this.plaintext));
    }

    @Test
    public final void testRoundtrip() {
        assertEquals(this.plaintext, this.codec.decrypt(this.codec.encrypt(this.plaintext)));
    }
}
