package edu.stanford.irt.laneweb.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TypeReferenceTest {

    private class TR<T> extends TypeReference<T> {
    }

    @Test
    public void testEquals() {
        TypeReference<?> other = new TypeReference<String>() {
        };
        assertEquals(new TypeReference<String>() {
        }, other);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(new TypeReference<String>() {
        }, null);
    }

    @Test
    public void testEqualsSame() {
        TypeReference<Object> typeReference = new TypeReference<Object>() {
        };
        assertEquals(typeReference, typeReference);
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
        assertNotEquals(new TypeReference<String>() {
        }, other);
    }

    @Test
    public void testTypeReference() {
        assertNotNull(new TR<String>() {
        });
    }
}
