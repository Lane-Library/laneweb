package edu.stanford.irt.laneweb.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.Assert;

public abstract class TypeReference<T> extends ParameterizedTypeReference<T> {

    private final Type type;

    protected TypeReference() {
        Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
        Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
        Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
        this.type = actualTypeArguments[0];
    }

    private static Class<?> findParameterizedTypeReferenceSubclass(final Class<?> child) {
        Class<?> parent = child.getSuperclass();
        if (Object.class == parent) {
            throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
        } else if (TypeReference.class == parent) {
            return child;
        } else {
            return findParameterizedTypeReferenceSubclass(parent);
        }
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
