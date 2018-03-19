package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EntityType_UnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void constructor_and_getters() {
	// Setup
	final String NAME = "MyEntity";
	final String ATTR_NAME1 = "AttrFloating";
	final String ATTR_NAME2 = "AttrStringList";
	final ValueType TYPE1 = new ValueType(Container.SINGLE, Primitive.FLOATING);
	final ValueType TYPE2 = new ValueType(Container.LIST, Primitive.STRING);
	final Attribute ATTR1 = new Attribute(ATTR_NAME1, TYPE1);
	final Attribute ATTR2 = new Attribute(ATTR_NAME2, TYPE2);

	// Execute
	EntityType entity = new EntityType(NAME, Arrays.asList(ATTR1, ATTR2));

	// Verify
	assertSame(NAME, entity.getName());
	assertSame(ATTR1, entity.getAttribute(ATTR_NAME1));
	assertSame(ATTR2, entity.getAttribute(ATTR_NAME2));
	assertSame(2, entity.getAttributes().size());

	assertSame(entity, ATTR1.getOwner());
	assertSame(entity, ATTR2.getOwner());
    }

    @Test
    public void constructor_failure_sharingAttribute() {
	// Setup
	final String NAME = "MyEntity";
	final String ATTR_NAME1 = "AttrFloating";
	final String ATTR_NAME2 = "AttrStringList";
	final ValueType TYPE1 = new ValueType(Container.SINGLE, Primitive.FLOATING);
	final ValueType TYPE2 = new ValueType(Container.LIST, Primitive.STRING);
	final Attribute ATTR1 = new Attribute(ATTR_NAME1, TYPE1);
	final Attribute ATTR2 = new Attribute(ATTR_NAME2, TYPE2);

	new EntityType(NAME, Arrays.asList(ATTR2));

	// Rule
	exception.expect(RuntimeException.class);
	exception.expectMessage("already attached to the Entity");

	// Execute
	new EntityType(NAME, Arrays.asList(ATTR1, ATTR2));
    }

    @Test
    public void equals_and_hash() {
	// Setup
	final String NAME1 = "Hello";
	final String NAME2 = "World";

	final String ATTR_NAME1 = "First";
	final String ATTR_NAME2 = "Second";
	final String ATTR_NAME3 = "Third";

	final ValueType TYPE1 = new ValueType(Container.SINGLE, Primitive.BOOLEAN);
	final ValueType TYPE2 = new ValueType(Container.LIST, Primitive.REFERENCE);
	final ValueType TYPE3 = new ValueType(Container.MAP, Primitive.TEXT);

	final EntityType ENTITY1 = new EntityType(NAME1,
		Arrays.asList(new Attribute(ATTR_NAME1, TYPE1), new Attribute(ATTR_NAME2, TYPE2)));

	final EntityType ENTITY2 = new EntityType(NAME1,
		Arrays.asList(new Attribute(ATTR_NAME2, TYPE2), new Attribute(ATTR_NAME3, TYPE3)));

	final EntityType ENTITY3 = new EntityType(NAME2,
		Arrays.asList(new Attribute(ATTR_NAME1, TYPE1), new Attribute(ATTR_NAME2, TYPE2)));

	final EntityType ENTITY4 = new EntityType(NAME2,
		Arrays.asList(new Attribute(ATTR_NAME2, TYPE2), new Attribute(ATTR_NAME3, TYPE3)));

	final EntityType ENTITY1_COPY = new EntityType(NAME1,
		Arrays.asList(new Attribute(ATTR_NAME2, TYPE2), new Attribute(ATTR_NAME1, TYPE1)));

	// Execute & Verify
	Asserts.assertEquality(ENTITY1, ENTITY1_COPY);

	Asserts.assertInequality(null, ENTITY1, ENTITY2, ENTITY3, ENTITY4);
    }
}
