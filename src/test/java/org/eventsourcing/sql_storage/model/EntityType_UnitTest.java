package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE;
import static org.eventsourcing.sql_storage.model.ValueType.STRING;
import static org.eventsourcing.sql_storage.model.ValueType.STRING_LIST;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EntityType_UnitTest {

    static final long TYPE_ID  = 1;
    static final long TYPE_ID1 = 2;
    static final long TYPE_ID2 = 3;

    static final String ENTITY_NAME  = "Entity";
    static final String ENTITY_NAME1 = "Hello";
    static final String ENTITY_NAME2 = "World";

    static final String ATTR_NAME1 = "first";
    static final String ATTR_NAME2 = "second";

    final Model MODEL1 = new Model.Builder()
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, STRING))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, STRING))
        .build();

    final Model MODEL2 = new Model.Builder()
        .type(TYPE_ID2, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, STRING))
        .type(TYPE_ID1, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, STRING))
        .build();

    final Model MODEL3 = new Model.Builder()
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, STRING))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, STRING)
            .attribute(ATTR_NAME2, INTEGER))
        .build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void builder_direct() {
        // Setup
        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Execute
        EntityType type = new EntityType.Builder()
            .typeId(TYPE_ID)
            .name(ENTITY_NAME)
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME2, STRING_LIST)
            .build(MODEL1, resolvers);

        // Verify
        assertSame(MODEL1, type.owner);
        assertSame(TYPE_ID, type.typeId);
        assertSame(ENTITY_NAME, type.name);

        assertSame(Attribute.ID, type.getAttribute(Attribute.ID).name);
        assertSame(REFERENCE, type.getAttribute(Attribute.ID).type);
        assertSame(0, type.getAttribute(Attribute.ID).relations.size());

        assertSame(ATTR_NAME1, type.getAttribute(ATTR_NAME1).name);
        assertSame(INTEGER, type.getAttribute(ATTR_NAME1).type);

        assertSame(ATTR_NAME2, type.getAttribute(ATTR_NAME2).name);
        assertSame(STRING_LIST, type.getAttribute(ATTR_NAME2).type);

        assertSame(3, type.attributes.size());
    }

    @Test
    public void builder_duplicate_attribute() {
        // Setup
        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("has duplicate Attribute names");

        // Execute
        new EntityType.Builder()
            .typeId(TYPE_ID)
            .name(ENTITY_NAME)
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME1, STRING_LIST)
            .build(MODEL1, resolvers);
    }

    @Test
    public void builder_no_id() {
        // Setup
        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no id specified");

        // Execute
        new EntityType.Builder()
            .name(ENTITY_NAME)
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME1, STRING_LIST)
            .build(MODEL1, resolvers);
    }

    @Test
    public void builder_no_name() {
        // Setup
        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no name specified");

        // Execute
        new EntityType.Builder()
            .typeId(TYPE_ID)
            .attribute(ATTR_NAME1, INTEGER)
            .attribute(ATTR_NAME1, STRING_LIST)
            .build(MODEL1, resolvers);
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final EntityType ENTITY_I1_N1_A1 = MODEL1.getEntityType(ENTITY_NAME1);
        final EntityType ENTITY_I2_N2_A1 = MODEL1.getEntityType(ENTITY_NAME2);
        final EntityType ENTITY_I2_N1_A1 = MODEL2.getEntityType(ENTITY_NAME1);
        final EntityType ENTITY_I1_N2_A1 = MODEL2.getEntityType(ENTITY_NAME2);
        final EntityType ENTITY_I2_N2_A2 = MODEL3.getEntityType(ENTITY_NAME2);
        final EntityType ENTITY_I1_N1_A1_COPY = MODEL3.getEntityType(ENTITY_NAME1);

        // Execute & Verify
        Asserts.assertEquality(ENTITY_I1_N1_A1, ENTITY_I1_N1_A1_COPY);

        Asserts.assertInequality(null, ENTITY_I1_N1_A1, ENTITY_I2_N2_A1, ENTITY_I2_N1_A1, ENTITY_I1_N2_A1,
            ENTITY_I2_N2_A2);
    }
}
