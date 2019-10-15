package org.eventsourcing.sql_storage.model;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Primitive_UnitTest {

    @Test
    public void codeCoverage() {
        // Execute
        Asserts.assertEnum(Primitive.class);
    }
}
