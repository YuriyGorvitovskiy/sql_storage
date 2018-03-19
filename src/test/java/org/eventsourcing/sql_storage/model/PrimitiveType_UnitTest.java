package org.eventsourcing.sql_storage.model;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class PrimitiveType_UnitTest {

    @Test
    public void codeCoverage() {
	// Execute
	Asserts.assertEnum(PrimitiveType.class);
    }
}
