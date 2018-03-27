package org.eventsourcing.sql_storage.database;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Postgres_UnitTest {

    @Test
    public void test_DATATBASE() {
        // Verify
        assertEquals(63, Postgres.DATABASE.maxidentifierLength);
        assertEquals(756, Postgres.DATABASE.reservedKeywords.size());
    }

}
