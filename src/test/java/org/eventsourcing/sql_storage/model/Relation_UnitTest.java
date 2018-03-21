package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class Relation_UnitTest {

    @Test
    public void constructor_and_getters() {
        // Setup
        final String ENTITY_NAME = "MyEntity";
        final String ATTR_NAME = "MyAttr";

        // Execute
        Relation target = new Relation(ENTITY_NAME, ATTR_NAME);

        // Verify
        assertSame(ENTITY_NAME, target.getTargetEntityName());
        assertSame(ATTR_NAME, target.getReverseAttributeName());
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final String ENTITY_NAME1 = "Hello";
        final String ENTITY_NAME2 = "World";

        final String ATTR_NAME1 = "first";
        final String ATTR_NAME2 = "second";

        final Relation TARGET_E1_A1 = new Relation(ENTITY_NAME1, ATTR_NAME1);
        final Relation TARGET_E1_A2 = new Relation(ENTITY_NAME1, ATTR_NAME2);
        final Relation TARGET_E2_A1 = new Relation(ENTITY_NAME2, ATTR_NAME1);
        final Relation TARGET_E2_A2 = new Relation(ENTITY_NAME2, ATTR_NAME2);
        final Relation TARGET_E1_A1_COPY = new Relation(ENTITY_NAME1, ATTR_NAME1);

        // Execute & Verify
        Asserts.assertEquality(TARGET_E1_A1, TARGET_E1_A1_COPY);

        Asserts.assertInequality(null, TARGET_E1_A1, TARGET_E1_A2, TARGET_E2_A1, TARGET_E2_A2);
    }

}
