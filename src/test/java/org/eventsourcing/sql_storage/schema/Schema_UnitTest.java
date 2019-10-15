package org.eventsourcing.sql_storage.schema;

import static org.eventsourcing.sql_storage.schema.DataType.BOOLEAN;
import static org.eventsourcing.sql_storage.schema.DataType.FLOATING;
import static org.eventsourcing.sql_storage.schema.DataType.INTEGER;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_2;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_3;
import static org.eventsourcing.sql_storage.schema.Example.SCHEMA_1;
import static org.eventsourcing.sql_storage.schema.Example.SCHEMA_2;
import static org.eventsourcing.sql_storage.schema.Example.SCHEMA_3;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_NAME_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Schema_UnitTest {

    @Test
    public void builder_getters() {
        // Execute
        Schema schema = new Schema.Builder()
            .table(TABLE_NAME_1,
                    t -> t
                        .column(COLUMN_NAME_1, INTEGER))
            .table(TABLE_NAME_2,
                    t -> t
                        .column(COLUMN_NAME_2, FLOATING))
            .build();

        // Verify

        assertEquals(TABLE_NAME_1, schema.getTable(TABLE_NAME_1).name);
        assertEquals(TABLE_NAME_2, schema.getTable(TABLE_NAME_2).name);
        assertEquals(2, schema.tables.size());
    }

    @Test
    public void builder_duplicate_table() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Schema.Builder()
                    .table(TABLE_NAME_1, t -> t.column(COLUMN_NAME_1, BOOLEAN))
                    .table(TABLE_NAME_2, t -> t.column(COLUMN_NAME_2, INTEGER))
                    .table(TABLE_NAME_1, t -> t.column(COLUMN_NAME_3, FLOATING))
                    .build());

        // Verify
        assertTrue(thrown.getMessage().contains("duplicate table"));
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(SCHEMA_1, SCHEMA_3);

        Asserts.assertInequality(null, SCHEMA_1, SCHEMA_2);
    }
}
