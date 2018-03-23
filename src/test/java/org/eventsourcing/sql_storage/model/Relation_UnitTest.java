package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_LIST;
import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Relation_UnitTest {
    static final long TYPE_ID  = 1;
    static final long TYPE_ID1 = 2;
    static final long TYPE_ID2 = 3;

    static final String ENTITY_NAME  = "Entity";
    static final String ENTITY_NAME1 = "Hello";
    static final String ENTITY_NAME2 = "World";

    static final String ATTR_NAME1 = "first";
    static final String ATTR_NAME2 = "second";

    final Model MODEL = new Model.Builder()
        .type(TYPE_ID, ENTITY_NAME, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, ENTITY_NAME, ATTR_NAME2)
            .attribute(ATTR_NAME2, REFERENCE_LIST, ENTITY_NAME, ATTR_NAME1))
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, INTEGER))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, INTEGER))
        .build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void builder_direct() {
        // Setup
        final EntityType ENTITY = MODEL.getEntityType(ENTITY_NAME);
        final Attribute ATTR = ENTITY.getAttribute(ATTR_NAME1);

        // Execute
        Relation relation = new Relation.Builder()
            .target(ENTITY_NAME)
            .reverse(ATTR_NAME1)
            .build(MODEL);

        // Verify
        assertSame(ENTITY, relation.target);
        assertSame(ATTR, relation.reverse);
    }

    @Test
    public void builder_wrong_target() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("doesn't exists");

        // Execute
        new Relation.Builder()
            .target("NOT EXISTS")
            .reverse(ATTR_NAME1)
            .build(MODEL);
    }

    @Test
    public void builder_no_target() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("doesn't exists");

        // Execute
        new Relation.Builder()
            .reverse(ATTR_NAME1)
            .build(MODEL);
    }

    @Test
    public void builder_wrong_reverse() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("doesn't exists");

        // Execute
        new Relation.Builder()
            .target(ENTITY_NAME)
            .reverse("NOT EXISTS")
            .build(MODEL);
    }

    @Test
    public void builder_no_reverse() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("doesn't exists");

        // Execute
        new Relation.Builder()
            .target(ENTITY_NAME)
            .build(MODEL);
    }

    @Test
    public void builder_wrong_reverse_type() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage(" but should be " + Primitive.REFERENCE);

        // Execute
        new Relation.Builder()
            .target(ENTITY_NAME1)
            .reverse(ATTR_NAME1)
            .build(MODEL);
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final EntityType ENTITY1 = MODEL.getEntityType(ENTITY_NAME1);
        final EntityType ENTITY2 = MODEL.getEntityType(ENTITY_NAME2);

        final Attribute ENTITY1_ATTR1 = ENTITY1.getAttribute(ATTR_NAME1);
        final Attribute ENTITY1_ATTR2 = ENTITY1.getAttribute(ATTR_NAME2);
        final Attribute ENTITY2_ATTR1 = ENTITY1.getAttribute(ATTR_NAME1);
        final Attribute ENTITY2_ATTR2 = ENTITY1.getAttribute(ATTR_NAME2);

        final Relation TARGET_E1_A1 = new Relation(ENTITY1, ENTITY1_ATTR1);
        final Relation TARGET_E1_A2 = new Relation(ENTITY1, ENTITY1_ATTR2);
        final Relation TARGET_E2_A1 = new Relation(ENTITY2, ENTITY2_ATTR1);
        final Relation TARGET_E2_A2 = new Relation(ENTITY2, ENTITY2_ATTR2);
        final Relation TARGET_E1_A1_COPY = new Relation(ENTITY1, ENTITY1_ATTR1);

        // Execute & Verify
        Asserts.assertEquality(TARGET_E1_A1, TARGET_E1_A1_COPY);

        Asserts.assertInequality(null, TARGET_E1_A1, TARGET_E1_A2, TARGET_E2_A1, TARGET_E2_A2);
    }

}
