package com.mdt.common.functional;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Throwable;
}
