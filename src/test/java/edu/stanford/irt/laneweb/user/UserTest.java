package edu.stanford.irt.laneweb.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        this.user = new User("id", "name", "email", "hashkey");
    }

    @Test
    public void testEqualsNotUser() {
        assertFalse(this.user.equals(new Object()));
    }

    @Test
    public void testEqualsObjectDifferentId() {
        assertFalse(this.user.equals(new User("di", null, null, null)));
    }

    @Test
    public void testEqualsObjectSameId() {
        assertTrue(this.user.equals(new User("id", null, null, null)));
    }

    @Test
    public void testGetEmail() {
        assertEquals("email", this.user.getEmail());
    }

    @Test
    public void testGetHashedId() {
        assertEquals("307d214168011862c7f0a95ee0ece4e6", this.user.getHashedId());
        User another = new User("id@name.org", "name", "email", "hashkey");
        assertEquals("307d214168011862c7f0a95ee0ece4e6@name.org", another.getHashedId());
    }

    @Test
    public void testGetId() {
        assertEquals("id", this.user.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("name", this.user.getName());
    }

    @Test
    public void testHashCode() {
        assertEquals(this.user.hashCode(), new User("id", null, null, null).hashCode());
    }

    @Test
    public void testStanfordEduId() {
        assertEquals("sunetid", new User("sunetid@stanford.edu", null, null, null).getId());
    }
}
