package org.eventsourcing.sql_storage.database.postgres;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Postgres_UnitTest {

    @Test
    public void test_DATATBASE() {
        // Verify
        assertEquals(63, new Postgres().maxIdentifierLength);
        assertEquals(756, new Postgres().reservedKeywords.size());
    }

}
