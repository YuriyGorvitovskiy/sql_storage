package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class ValueType_UnitTest {

    @Test
    public void constructor_and_getters() {
	// Setup
	final ContainerType CONTAINER = ContainerType.MAP;
	final PrimitiveType PRIMITIVE = PrimitiveType.FLOATING;

	// Execute
	ValueType type = new ValueType(CONTAINER, PRIMITIVE);

	// Verify
	assertSame(CONTAINER, type.getContainerType());
	assertSame(PRIMITIVE, type.getPrimitiveType());
    }

    @Test
    public void equals_and_hash() {
	// Setup
	final ContainerType CONTAINER1 = ContainerType.MAP;
	final ContainerType CONTAINER2 = ContainerType.SINGLE;

	final PrimitiveType PRIMITIVE1 = PrimitiveType.INTEGER;
	final PrimitiveType PRIMITIVE2 = PrimitiveType.REFERENCE;

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
