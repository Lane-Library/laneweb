package edu.stanford.irt.laneweb.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.core.ParameterizedTypeReference;

public abstract class TypeReference<T> extends ParameterizedTypeReference<T> {

    private final Type type;

    protected TypeReference() {
        Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
        ParameterizedType parameterizedType = (ParameterizedType) parameterizedTypeReferenceSubclass
                .getGenericSuperclass();
        this.type = parameterizedType.getActualTypeArguments()[0];
    }

    private static Class<?> findParameterizedTypeReferenceSubclass(final Class<?> child) {
        Class<?> parent = child.getSuperclass();
        if (TypeReference.class == parent) {
            return child;
        } else {
            return findParameterizedTypeReferenceSubclass(parent);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj || (obj instanceof TypeReference && this.type.equals(((TypeReference<?>) obj).type)));
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
