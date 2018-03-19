package org.eventsourcing.sql_storage.model;

import org.eventsourcing.sql_storage.model.Primitive;
import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class Primitive_UnitTest {

    @Test
    public void codeCoverage() {
	Asserts.assertEnum(Primitive.class);
    }
}
