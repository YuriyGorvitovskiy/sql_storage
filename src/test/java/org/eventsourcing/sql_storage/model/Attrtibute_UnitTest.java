package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_LIST;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_MAP;
import static org.eventsourcing.sql_storage.model.ValueType.STRING;
import static org.eventsourcing.sql_storage.model.ValueType.STRING_LIST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Attrtibute_UnitTest {

    static final long TYPE_ID1 = 1;
    static final long TYPE_ID2 = 2;
    static final long TYPE_ID3 = 3;

    static final String ENTITY_NAME1 = "Hello";
    static final String ENTITY_NAME2 = "World";
    static final String ENTITY_NAME3 = "Entity";

    static final String ATTR_NAME  = "attr";
    static final String ATTR_NAME1 = "first";
    static final String ATTR_NAME2 = "second";
    static final String ATTR_NAME3 = "third";

    final Model MODEL1 = new Model.Builder()
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, (a) -> a
                .relation(ENTITY_NAME2, ATTR_NAME1)
                .relation(ENTITY_NAME3, ATTR_NAME1))
            .attribute(ATTR_NAME2, INTEGER)
            .attribute(ATTR_NAME3, STRING))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, (a) -> a
                .relation(ENTITY_NAME1, ATTR_NAME1))
            .attribute(ATTR_NAME2, INTEGER)
            .attribute(ATTR_NAME3, STRING))
        .type(TYPE_ID3, ENTITY_NAME3, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, (a) -> a
                .relation(ENTITY_NAME1, ATTR_NAME1))
            .attribute(ATTR_NAME2, STRING)
            .attribute(ATTR_NAME3, INTEGER))
        .build();

    final Model MODEL2 = new Model.Builder()
        .type(TYPE_ID1, ENTITY_NAME1, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, (a) -> a
                .relation(ENTITY_NAME2, ATTR_NAME1)
                .relation(ENTITY_NAME3, ATTR_NAME1))
            .attribute(ATTR_NAME2, INTEGER)
            .attribute(ATTR_NAME3, STRING))
        .type(TYPE_ID2, ENTITY_NAME2, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, (a) -> a
                .relation(ENTITY_NAME1, ATTR_NAME1))
            .attribute(ATTR_NAME2, INTEGER)
            .attribute(ATTR_NAME3, STRING))
        .type(TYPE_ID3, ENTITY_NAME3, (t) -> t
            .attribute(ATTR_NAME1, REFERENCE, (a) -> a
                .relation(ENTITY_NAME1, ATTR_NAME1))
            .attribute(ATTR_NAME2, STRING)
            .attribute(ATTR_NAME3, INTEGER))
        .build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void builder_direct() {
        // Setup
        final EntityType ENTITY1 = MODEL1.getEntityType(ENTITY_NAME1);
        final EntityType ENTITY3 = MODEL1.getEntityType(ENTITY_NAME3);

        final Attribute ATTR1 = ENTITY1.getAttribute(ATTR_NAME1);
        final Attribute ATTR3 = ENTITY3.getAttribute(ATTR_NAME1);

        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Execute
        Attribute attribute = new Attribute.Builder()
            .name(ATTR_NAME)
            .type(REFERENCE_MAP)
            .relation(ENTITY_NAME1, ATTR_NAME1)
            .relation(ENTITY_NAME3, ATTR_NAME1)
            .build(ENTITY1, resolvers);

        for (Consumer<Model> resolver : resolvers) {
            resolver.accept(MODEL1);
        }

        // Verify
        assertSame(ENTITY1, attribute.owner);
        assertSame(ATTR_NAME, attribute.name);
        assertSame(REFERENCE_MAP, attribute.type);
        assertSame(ENTITY1, attribute.getRelation(ENTITY_NAME1).target);
        assertSame(ATTR1, attribute.getRelation(ENTITY_NAME1).reverse);
        assertSame(ENTITY3, attribute.getRelation(ENTITY_NAME3).target);
        assertSame(ATTR3, attribute.getRelation(ENTITY_NAME3).reverse);
        assertEquals(2, attribute.relations.size());
    }

    @Test
    public void builder_no_name() {
        // Setup
        final EntityType ENTITY1 = MODEL1.getEntityType(ENTITY_NAME1);

        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no name specified");

        // Execute
        new Attribute.Builder()
            .type(REFERENCE_MAP)
            .build(ENTITY1, resolvers);
    }

    @Test
    public void builder_reference_no_relation() {
        // Setup
        final EntityType ENTITY1 = MODEL1.getEntityType(ENTITY_NAME1);

        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("has no relation defined");

        // Execute
        new Attribute.Builder()
            .name(ATTR_NAME)
            .type(REFERENCE_MAP)
            .build(ENTITY1, resolvers);
    }

    @Test
    public void builder_non_reference_has_relations() {
        // Setup
        final EntityType ENTITY1 = MODEL1.getEntityType(ENTITY_NAME1);

        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("is not allowed to have relations");

        // Execute
        new Attribute.Builder()
            .name(ATTR_NAME)
            .type(STRING_LIST)
            .relation(ENTITY_NAME1, ATTR_NAME1)
            .build(ENTITY1, resolvers);
    }

    @Test
    public void builder_duplicate_relation_target() {
        // Setup
        final EntityType ENTITY1 = MODEL1.getEntityType(ENTITY_NAME1);

        final List<Consumer<Model>> resolvers = new ArrayList<>();

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("has duplicate target entities");

        // Execute
        new Attribute.Builder()
            .name(ATTR_NAME)
            .type(REFERENCE_LIST)
            .relation(ENTITY_NAME1, ATTR_NAME1)
            .relation(ENTITY_NAME1, ATTR_NAME1)
            .build(ENTITY1, resolvers);

        for (Consumer<Model> resolver : resolvers) {
            resolver.accept(MODEL1);
        }

    }

    @Test
    public void equals_and_hash() {
        // Setup
        final Attribute ATTR_N1_T1_R1 = MODEL1.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME1);
        final Attribute ATTR_N2_T2_RN = MODEL1.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME2);
        final Attribute ATTR_N3_T3_RN = MODEL1.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME3);

        final Attribute ATTR_N1_T1_R1_COPY = MODEL2.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME1);
        final Attribute ATTR_N2_T2_RN_COPY = MODEL2.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME2);
        final Attribute ATTR_N3_T3_RN_COPY = MODEL2.getEntityType(ENTITY_NAME1).getAttribute(ATTR_NAME3);

        final Attribute ATTR_N1_T1_R2 = MODEL1.getEntityType(ENTITY_NAME3).getAttribute(ATTR_NAME1);
        final Attribute ATTR_N2_T3_RN = MODEL1.getEntityType(ENTITY_NAME3).getAttribute(ATTR_NAME2);
        final Attribute ATTR_N3_T2_RN = MODEL1.getEntityType(ENTITY_NAME3).getAttribute(ATTR_NAME3);

        // Execute & Verify
        Asserts.assertEquality(ATTR_N1_T1_R1, ATTR_N1_T1_R1_COPY);
        Asserts.assertEquality(ATTR_N2_T2_RN, ATTR_N2_T2_RN_COPY);
        Asserts.assertEquality(ATTR_N3_T3_RN, ATTR_N3_T3_RN_COPY);

        Asserts.assertInequality(null,
            ATTR_N1_T1_R1, ATTR_N2_T2_RN, ATTR_N3_T3_RN,
            ATTR_N1_T1_R2, ATTR_N2_T3_RN, ATTR_N3_T2_RN);
    }

}
