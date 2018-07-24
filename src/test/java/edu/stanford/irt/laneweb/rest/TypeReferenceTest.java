package edu.stanford.irt.laneweb.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeReferenceTest {

    private class TR<T> extends TypeReference<T> {
    }

    @Test
    public void testEquals() {
        TypeReference<?> other = new TypeReference<String>() {
        };
        assertTrue(new TypeReference<String>() {
        }.equals(other));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(new TypeReference<String>() {
        }.equals(null));
    }

    @Test
    public void testEqualsSame() {
        TypeReference<Object> typeReference = new TypeReference<Object>() {
        };
        assertTrue(typeReference.equals(typeReference));
    }

    @Test
    public void testGetType() {
        assertSame(String.class, new TypeReference<String>() {
        }.getType());
    }

    @Test
    public void testHashCode() {
        TypeReference<Object> typeReference = new TypeReference<Object>() {
        };
        assertEquals(typeReference.getType().hashCode(), typeReference.hashCode());
    }

    @Test
    public void testNotEquals() {
        TypeReference<?> other = new TypeReference<Integer>() {
        };
        assertFalse(new TypeReference<String>() {
        }.equals(other));
    }

    @Test
    public void testTypeReference() {
        assertNotNull(new TR<String>() {
        });
    }
}
