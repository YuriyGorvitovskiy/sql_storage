package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.FLOATING;
import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AttrtibuteId_UnitTest {

    static final long TYPE_ID1 = 1;
    static final long TYPE_ID2 = 2;

    static final String ENTITY_NAME1 = "Hello";
    static final String ENTITY_NAME2 = "World";

    static final String ATTR_NAME1 = "first";
    static final String ATTR_NAME2 = "second";

    final Model MODEL1 = new Model.Builder()
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, FLOATING)
            .attribute(ATTR_NAME2, INTEGER))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, FLOATING)
            .attribute(ATTR_NAME2, INTEGER))
        .build();

    final Model MODEL2 = new Model.Builder()
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, FLOATING))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME2, INTEGER))
        .build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void builder_direct() {
        // Setup
        final Attribute ATTR1 = MODEL1.getAttribute(ENTITY_NAME1, ATTR_NAME1);

        // Execute
        AttributeId id1 = new AttributeId(ATTR1);

        // Verify
        assertSame(ENTITY_NAME1, id1.className);
        assertSame(ATTR_NAME1, id1.attributeName);
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final AttributeId ATTR_M1_T1_A1 = new AttributeId(MODEL1.getAttribute(ENTITY_NAME1, ATTR_NAME1));
        final AttributeId ATTR_M1_T1_A2 = new AttributeId(MODEL1.getAttribute(ENTITY_NAME1, ATTR_NAME2));

        final AttributeId ATTR_M1_T2_A1 = new AttributeId(MODEL1.getAttribute(ENTITY_NAME2, ATTR_NAME1));
        final AttributeId ATTR_M1_T2_A2 = new AttributeId(MODEL1.getAttribute(ENTITY_NAME2, ATTR_NAME2));

        final AttributeId ATTR_M2_T1_A1 = new AttributeId(MODEL2.getAttribute(ENTITY_NAME1, ATTR_NAME1));
        final AttributeId ATTR_M2_T2_A2 = new AttributeId(MODEL2.getAttribute(ENTITY_NAME2, ATTR_NAME2));

        // Execute & Verify
        Asserts.assertEquality(ATTR_M1_T1_A1, ATTR_M2_T1_A1);
        Asserts.assertEquality(ATTR_M1_T2_A2, ATTR_M2_T2_A2);

        Asserts.assertInequality(null, ATTR_M1_T1_A1, ATTR_M1_T1_A2, ATTR_M1_T2_A1, ATTR_M1_T2_A2);
    }

}
