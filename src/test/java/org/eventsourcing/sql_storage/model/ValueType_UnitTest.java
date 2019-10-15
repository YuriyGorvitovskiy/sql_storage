package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.Container.LIST;
import static org.eventsourcing.sql_storage.model.Container.MAP;
import static org.eventsourcing.sql_storage.model.Container.SINGLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class ValueType_UnitTest {

    @Test
    public void typeOf_primitive_container() {
        for (Primitive primitive : Primitive.values()) {
            for (Container container : Container.values()) {
                // Execute
                ValueType type = ValueType.typeOf(primitive, container);

                // Verify
                assertSame(primitive, type.primitive);
                assertSame(container, type.container);
            }
        }
    }

    @Test
    public void typeOf_primitive() {
        for (Primitive primitive : Primitive.values()) {
            // Execute
            ValueType type = ValueType.typeOf(primitive);

            // Verify
            assertSame(primitive, type.primitive);
            assertSame(SINGLE, type.container);
        }
    }

    @Test
    public void listOf() {
        for (Primitive primitive : Primitive.values()) {
            // Execute
            ValueType type = ValueType.listOf(primitive);

            // Verify
            assertSame(primitive, type.primitive);
            assertSame(LIST, type.container);
        }
    }

    @Test
    public void mapOf() {
        for (Primitive primitive : Primitive.values()) {
            // Execute
            ValueType type = ValueType.mapOf(primitive);

            // Verify
            assertSame(primitive, type.primitive);
            assertSame(MAP, type.container);
        }
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final ValueType TYPE_P1_C1      = ValueType.INTEGER;
        final ValueType TYPE_P1_C2      = ValueType.INTEGER_MAP;
        final ValueType TYPE_P2_C1      = ValueType.REFERENCE;
        final ValueType TYPE_P2_C2      = ValueType.REFERENCE_MAP;
        final ValueType TYPE_P1_C1_COPY = ValueType.INTEGER;

        // Execute & Verify
        Asserts.assertEquality(TYPE_P1_C1, TYPE_P1_C1_COPY);

        Asserts.assertInequality(null, TYPE_P1_C1, TYPE_P1_C2, TYPE_P2_C1, TYPE_P2_C2);
    }

    @Test
    public void to_string() {
        // Execute & Verify
        assertEquals("TEXT", ValueType.TEXT.toString());
        assertEquals("FLOATING-LIST", ValueType.FLOATING_LIST.toString());
        assertEquals("BOOLEAN-MAP", ValueType.BOOLEAN_MAP.toString());

    }

}
