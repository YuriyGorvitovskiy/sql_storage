package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Model_UnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void constructor_and_getters() {
        // Setup
        final String TYPE_NAME1 = "Hello";
        final String TYPE_NAME2 = "World";
        final EntityType TYPE1 = new EntityType(TYPE_NAME1, Collections.emptyList());
        final EntityType TYPE2 = new EntityType(TYPE_NAME2, Collections.emptyList());

        // Execute
        Model model = new Model(Arrays.asList(TYPE1, TYPE2));

        // Verify
        assertSame(TYPE1, model.getEntityType(TYPE_NAME1));
        assertSame(TYPE2, model.getEntityType(TYPE_NAME2));
        assertSame(2, model.getEntityTypes().size());

        assertSame(model, TYPE1.getOwner());
        assertSame(model, TYPE1.getOwner());
    }

    @Test
    public void constructor_failure_sharingEntityType() {
        // Setup
        final String TYPE_NAME1 = "Hello";
        final String TYPE_NAME2 = "World";
        final EntityType TYPE1 = new EntityType(TYPE_NAME1, Collections.emptyList());
        final EntityType TYPE2 = new EntityType(TYPE_NAME2, Collections.emptyList());

        new Model(Arrays.asList(TYPE2));

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("already attached to the Model");

        // Execute
        new Model(Arrays.asList(TYPE1, TYPE2));
    }

    @Test
    public void constructor_failure_duplicateClassName() {
        // Setup
        final String TYPE_NAME = "Hello";
        final EntityType TYPE1 = new EntityType(TYPE_NAME, Collections.emptyList());
        final EntityType TYPE2 = new EntityType(TYPE_NAME, Collections.emptyList());

        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("duplicate Entity Type names");

        // Execute
        new Model(Arrays.asList(TYPE1, TYPE2));
    }

    @Test
    public void equals_and_hash() {
        // Setup
        final String TYPE_NAME1 = "First";
        final String TYPE_NAME2 = "Second";
        final String TYPE_NAME3 = "Third";
        final List<Attribute> ATTRS = Collections.emptyList();

        final Model MODEL1 = new Model(
            Arrays.asList(new EntityType(TYPE_NAME1, ATTRS), new EntityType(TYPE_NAME2, ATTRS)));

        final Model MODEL2 = new Model(
            Arrays.asList(new EntityType(TYPE_NAME2, ATTRS), new EntityType(TYPE_NAME3, ATTRS)));

        final Model MODEL1_COPY = new Model(
            Arrays.asList(new EntityType(TYPE_NAME2, ATTRS), new EntityType(TYPE_NAME1, ATTRS)));

        // Execute & Verify
        Asserts.assertEquality(MODEL1, MODEL1_COPY);

        Asserts.assertInequality(null, MODEL1, MODEL2);
    }
}
