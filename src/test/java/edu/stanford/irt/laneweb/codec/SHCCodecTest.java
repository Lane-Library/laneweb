/**
 *
 */
package edu.stanford.irt.laneweb.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ryanmax
 */
public class SHCCodecTest {

    private String ciphertext = "9muniFwV90BgjxJpnNC2pE/AY8pWD5iajvRw2uCTOfg=";

    private SHCCodec codec;

    private String plaintext = "plain piece of text";

    @Before
    public void setUp() {
        // note: not the real key and vector:
        this.codec = new SHCCodec("yfGIl68aDih3DamkzIJeYA==", "ABCDEFGHIJKLMNOP");
    }

    @Test
    public void testDecrypt() {
        assertEquals(this.plaintext, this.codec.decrypt(this.ciphertext));
    }

    @Test
    public void testEncrypt() {
        assertEquals(this.ciphertext, this.codec.encrypt(this.plaintext));
    }

    @Test
    public void testLongParameters() {
        assertNotNull(new SHCCodec("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"));
    }

    @Test
    public void testRoundtrip() {
        assertEquals(this.plaintext, this.codec.decrypt(this.codec.encrypt(this.plaintext)));
    }
}
