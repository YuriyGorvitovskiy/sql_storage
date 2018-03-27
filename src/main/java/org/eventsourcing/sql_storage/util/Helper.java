package org.eventsourcing.sql_storage.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Helper {

    public static final String UTF_8 = StandardCharsets.UTF_8.name();

    private Helper() {
    }

    public static <T> List<T> valuesOfPublicStaticFinalFields(Class<T> fieldType, Class<?> classToScan) {
        List<T> values = new ArrayList<>();
        for (Field field : classToScan.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)
                    || !Modifier.isFinal(modifiers)
                    || !Modifier.isStatic(modifiers))
                continue;

            T value = getStaticField(fieldType, field);
            if (null == value)
                continue;

            values.add(value);
        }
        return values;
    }

    public static <T> T getStaticField(Class<T> castTo, Field field) {
        try {
            return cast(castTo, field.get(null));
        } catch (Throwable ex) {}
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Class<T> castTo, Object value) {
        if (castTo.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    public static boolean isEmpty(String name) {
        return null == name || name.length() == 0;
    }

    public static String resourceAsString(Class<?> clazz, String resourceName) {
        return scanResource(clazz, resourceName, (s) -> s.useDelimiter("\\A").next());
    }

    public static List<String> resourceAsLines(Class<?> clazz, String resourceName) {
        return scanResource(clazz, resourceName, (s) -> {
            List<String> list = new ArrayList<>();
            while (s.hasNextLine()) {
                list.add(s.nextLine());
            }
            return list;
        });
    }

    public static Set<String> resourceAsDictionary(Class<?> clazz, String resourceName) {
        return resourceAsLines(clazz, resourceName).stream()
            .map(s -> s.trim().toLowerCase())
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    }

    public static <R> R scanResource(Class<?> clazz, String resourceName, Function<Scanner, R> process) {
        return processResource(
            () -> new Scanner(clazz.getResourceAsStream(resourceName), UTF_8),
            process,
            (e) -> {
                throw new RuntimeException(
                    "Failed to load resource '" + resourceName + "' from class " + clazz.getName(), e);
            });
    }

    public static <C extends AutoCloseable, R> R processResource(
            SupplierWithException<C> resourceSupplier,
            Function<C, R> process,
            Function<Throwable, R> errorHandling) {
        try (C resource = resourceSupplier.get()) {
            return process.apply(resource);
        } catch (Throwable ex) {
            return errorHandling.apply(ex);
        }
    }
}
