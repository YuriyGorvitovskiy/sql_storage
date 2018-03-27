package org.eventsourcing.sql_storage.util;

@FunctionalInterface
public interface SupplierWithException<T> {
    T get() throws Exception;
}
