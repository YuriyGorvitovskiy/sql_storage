package org.eventsourcing.sql_storage.schema;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Test;

public class DataType_UnitTest {

    @Test
    public void codeCoverage() {
        // Execute
        Asserts.assertEnum(DataType.class);
    }
}
