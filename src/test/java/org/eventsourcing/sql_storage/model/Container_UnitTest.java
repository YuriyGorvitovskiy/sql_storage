package org.eventsourcing.sql_storage.model;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Container_UnitTest {

    @Test
    public void codeCoverage() {
        // Execute
        Asserts.assertEnum(Container.class);
    }
}
