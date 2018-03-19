package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Attrtibute_UnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void constructor_and_getters() {
	// Setup
	final String ENTITY_NAME1 = "Hello";
	final String ENTITY_NAME2 = "World";

	final String ATTR_NAME1 = "first";
	final String ATTR_NAME2 = "second";

	final ValueType TYPE = new ValueType(Container.MAP, Primitive.FLOATING);

	final Relation TARGET1 = new Relation(ENTITY_NAME1, ATTR_NAME1);
	final Relation TARGET2 = new Relation(ENTITY_NAME2, ATTR_NAME2);

	// Execute
	Attribute attrSimple = new Attribute(ATTR_NAME1, TYPE);
	Attribute attrRelation = new Attribute(ATTR_NAME2, Container.LIST,
		Arrays.asList(TARGET1, TARGET2));

	// Verify
	assertSame(ATTR_NAME1, attrSimple.getName());
	assertSame(TYPE, attrSimple.getType());
	assertNull(attrSimple.getTargets());
	assertNull(attrSimple.getTarget(ENTITY_NAME1));

	assertSame(ATTR_NAME2, attrRelation.getName());
	assertEquals(new ValueType(Container.LIST, Primitive.REFERENCE), attrRelation.getType());
	assertSame(TARGET1, attrRelation.getTarget(ENTITY_NAME1));
	assertSame(TARGET2, attrRelation.getTarget(ENTITY_NAME2));
	assertEquals(2, attrRelation.getTargets().size());
    }

    @Test
    public void constructor_failure_sameTargetEntity() {
	// Setup
	final String ENTITY_NAME = "Hello";

	final String ATTR_NAME1 = "first";
	final String ATTR_NAME2 = "second";

	final Relation TARGET1 = new Relation(ENTITY_NAME, ATTR_NAME1);
	final Relation TARGET2 = new Relation(ENTITY_NAME, ATTR_NAME2);

	// Rule
	exception.expect(RuntimeException.class);
	exception.expectMessage("has duplicate target entities");

	// Execute
	new Attribute(ATTR_NAME2, Container.LIST, Arrays.asList(TARGET1, TARGET2));
    }

    @Test
    public void equals_and_hash() {
	// Setup
	final String ENTITY_NAME1 = "Hello";
	final String ENTITY_NAME2 = "World";

	final String ATTR_NAME1 = "first";
	final String ATTR_NAME2 = "second";

	final ValueType TYPE1 = new ValueType(Container.SINGLE, Primitive.BOOLEAN);

	final Relation TARGET1 = new Relation(ENTITY_NAME1, ATTR_NAME1);
	final Relation TARGET2 = new Relation(ENTITY_NAME2, ATTR_NAME2);

	final Attribute ATTR_N1_T1 = new Attribute(ATTR_NAME1, TYPE1);
	final Attribute ATTR_N1_T2 = new Attribute(ATTR_NAME1, Container.MAP,
		Arrays.asList(TARGET1, TARGET2));
	final Attribute ATTR_N2_T1 = new Attribute(ATTR_NAME2, TYPE1);
	final Attribute ATTR_N2_T2 = new Attribute(ATTR_NAME2, Container.MAP,
		Arrays.asList(TARGET1, TARGET2));
	final Attribute ATTR_N1_T1_COPY = new Attribute(ATTR_NAME1, TYPE1);
	final Attribute ATTR_N2_T2_COPY = new Attribute(ATTR_NAME2, Container.MAP,
		Arrays.asList(TARGET1, TARGET2));

	// Execute & Verify
	Asserts.assertEquality(ATTR_N1_T1, ATTR_N1_T1_COPY);
	Asserts.assertEquality(ATTR_N2_T2, ATTR_N2_T2_COPY);

	Asserts.assertInequality(null, ATTR_N1_T1, ATTR_N1_T2, ATTR_N2_T1, ATTR_N2_T2);
    }

}
