package edu.stanford.irt.laneweb.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = new User("id@domain", "name", "email", "hashkey");
    }

    @Test
    public void testEqualsNotUser() {
        assertNotEquals(this.user, new Object());
    }

    @Test
    public void testEqualsObjectDifferentId() {
        assertNotEquals(this.user, new User("di@domain", null, null, null));
    }

    @Test
    public void testEqualsObjectSameId() {
        assertEquals(this.user, new User("id@domain", null, null, null));
    }

    @Test
    public void testGetEmail() {
        assertEquals("email", this.user.getEmail());
    }

    @Test
    public void testGetHashedId() {
        assertEquals("307d214168011862c7f0a95ee0ece4e6@domain", this.user.getHashedId());
        User another = new User("id@name.org", "name", "email", "hashkey");
        assertEquals("307d214168011862c7f0a95ee0ece4e6@name.org", another.getHashedId());
    }

    @Test
    public void testGetId() {
        assertEquals("id@domain", this.user.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("name", this.user.getName());
    }

    @Test
    public void testHashCode() {
        assertEquals(this.user.hashCode(), new User("id@domain", null, null, null).hashCode());
    }

    @Test
    public void testStanfordEduId() {
        assertEquals("sunetid@stanford.edu", new User("sunetid@stanford.edu", null, null, null).getId());
    }
}
