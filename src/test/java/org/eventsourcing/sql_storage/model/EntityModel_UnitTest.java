package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EntityModel_UnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void constructor_and_getters() {
	// Setup
	final String NAME = "MyEntity";
	final String ATTR_NAME1 = "AttrFloating";
	final String ATTR_NAME2 = "AttrStringList";
	final ValueType TYPE1 = new ValueType(ContainerType.SINGLE, PrimitiveType.FLOATING);
	final ValueType TYPE2 = new ValueType(ContainerType.LIST, PrimitiveType.STRING);
	final AttributeModel ATTR1 = new AttributeModel(ATTR_NAME1, TYPE1);
	final AttributeModel ATTR2 = new AttributeModel(ATTR_NAME2, TYPE2);

	// Execute
	EntityModel entity = new EntityModel(NAME, Arrays.asList(ATTR1, ATTR2));

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
	final ValueType TYPE1 = new ValueType(ContainerType.SINGLE, PrimitiveType.FLOATING);
	final ValueType TYPE2 = new ValueType(ContainerType.LIST, PrimitiveType.STRING);
	final AttributeModel ATTR1 = new AttributeModel(ATTR_NAME1, TYPE1);
	final AttributeModel ATTR2 = new AttributeModel(ATTR_NAME2, TYPE2);

	new EntityModel(NAME, Arrays.asList(ATTR2));

	// Rule
	exception.expect(RuntimeException.class);
	exception.expectMessage("already attached to the Entity");

	// Execute
	new EntityModel(NAME, Arrays.asList(ATTR1, ATTR2));
    }

    @Test
    public void equals_and_hash() {
	// Setup
	final String NAME1 = "Hello";
	final String NAME2 = "World";

	final String ATTR_NAME1 = "First";
	final String ATTR_NAME2 = "Second";
	final String ATTR_NAME3 = "Third";

	final ValueType TYPE1 = new ValueType(ContainerType.SINGLE, PrimitiveType.BOOLEAN);
	final ValueType TYPE2 = new ValueType(ContainerType.LIST, PrimitiveType.REFERENCE);
	final ValueType TYPE3 = new ValueType(ContainerType.MAP, PrimitiveType.TEXT);

	final EntityModel ENTITY1 = new EntityModel(NAME1,
		Arrays.asList(new AttributeModel(ATTR_NAME1, TYPE1), new AttributeModel(ATTR_NAME2, TYPE2)));

	final EntityModel ENTITY2 = new EntityModel(NAME1,
		Arrays.asList(new AttributeModel(ATTR_NAME2, TYPE2), new AttributeModel(ATTR_NAME3, TYPE3)));

	final EntityModel ENTITY3 = new EntityModel(NAME2,
		Arrays.asList(new AttributeModel(ATTR_NAME1, TYPE1), new AttributeModel(ATTR_NAME2, TYPE2)));

	final EntityModel ENTITY4 = new EntityModel(NAME2,
		Arrays.asList(new AttributeModel(ATTR_NAME2, TYPE2), new AttributeModel(ATTR_NAME3, TYPE3)));

	final EntityModel ENTITY1_COPY = new EntityModel(NAME1,
		Arrays.asList(new AttributeModel(ATTR_NAME2, TYPE2), new AttributeModel(ATTR_NAME1, TYPE1)));

	// Execute & Verify
	Asserts.assertEquality(ENTITY1, ENTITY1_COPY);

	Asserts.assertInequality(null, ENTITY1, ENTITY2, ENTITY3, ENTITY4);
    }
}
