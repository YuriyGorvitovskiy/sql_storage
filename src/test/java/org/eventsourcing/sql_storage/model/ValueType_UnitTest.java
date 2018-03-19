package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class ValueType_UnitTest {

    @Test
    public void constructor_and_getters() {
	// Setup
	final Container CONTAINER = Container.MAP;
	final Primitive PRIMITIVE = Primitive.FLOATING;

	// Execute
	ValueType type = new ValueType(CONTAINER, PRIMITIVE);

	// Verify
	assertSame(CONTAINER, type.getContainerType());
	assertSame(PRIMITIVE, type.getPrimitiveType());
    }

    @Test
    public void equals_and_hash() {
	// Setup
	final Container CONTAINER1 = Container.MAP;
	final Container CONTAINER2 = Container.SINGLE;

	final Primitive PRIMITIVE1 = Primitive.INTEGER;
	final Primitive PRIMITIVE2 = Primitive.REFERENCE;

	final ValueType TYPE_C1_P1 = new ValueType(CONTAINER1, PRIMITIVE1);
	final ValueType TYPE_C1_P2 = new ValueType(CONTAINER1, PRIMITIVE2);
	final ValueType TYPE_C2_P1 = new ValueType(CONTAINER2, PRIMITIVE1);
	final ValueType TYPE_C2_P2 = new ValueType(CONTAINER2, PRIMITIVE2);
	final ValueType TYPE_C1_P1_COPY = new ValueType(CONTAINER1, PRIMITIVE1);

	// Execute & Verify
	Asserts.assertEquality(TYPE_C1_P1, TYPE_C1_P1_COPY);

	Asserts.assertInequality(null, TYPE_C1_P1, TYPE_C1_P2, TYPE_C2_P1, TYPE_C2_P2);
    }

}
