package org.eventsourcing.sql_storage.model;

import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class AttrtibuteModel_UnitTest {

    @Test
    public void constructor_and_getters() {
	// Setup
	final String NAME = "MyAttribute";
	final ValueType TYPE = new ValueType(ContainerType.MAP, PrimitiveType.FLOATING);

	// Execute
	AttributeModel attr = new AttributeModel(NAME, TYPE);

	// Verify
	assertSame(NAME, attr.getName());
	assertSame(TYPE, attr.getType());
    }

    @Test
    public void equals_and_hash() {
	// Setup
	final String NAME1 = "Hello";
	final String NAME2 = "World";

	final ValueType TYPE1 = new ValueType(ContainerType.SINGLE, PrimitiveType.BOOLEAN);
	final ValueType TYPE2 = new ValueType(ContainerType.MAP, PrimitiveType.REFERENCE);

	final AttributeModel ATTR_N1_T1 = new AttributeModel(NAME1, TYPE1);
	final AttributeModel ATTR_N1_T2 = new AttributeModel(NAME1, TYPE2);
	final AttributeModel ATTR_N2_T1 = new AttributeModel(NAME2, TYPE1);
	final AttributeModel ATTR_N2_T2 = new AttributeModel(NAME2, TYPE2);
	final AttributeModel ATTR_N1_T1_COPY = new AttributeModel(NAME1, TYPE1);

	// Execute & Verify
	Asserts.assertEquality(ATTR_N1_T1, ATTR_N1_T1_COPY);

	Asserts.assertInequality(null, ATTR_N1_T1, ATTR_N1_T2, ATTR_N2_T1, ATTR_N2_T2);
    }

}
