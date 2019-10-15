package org.eventsourcing.sql_storage.schema;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class DataType_UnitTest {

    @Test
    public void codeCoverage() {
        // Execute
        Asserts.assertEnum(DataType.class);
    }
}
