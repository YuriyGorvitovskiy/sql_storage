package org.eventsourcing.sql_storage.schema;

import static org.eventsourcing.sql_storage.schema.DataType.FLOATING;
import static org.eventsourcing.sql_storage.schema.DataType.TEXT;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_1_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_1_2;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_1_3;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_2_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_2_2;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_2_3;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_3_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_3_2;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_3_3;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_1_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_NAME_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Column_UnitTest {

    @Test
    public void builder_getters() {
        // Execute
        Schema schema = new Schema.Builder()
            .table(TABLE_NAME_1,
                    t -> t
                        .column(COLUMN_NAME_1, FLOATING))
            .build();

        // Verify
        Table  table  = schema.getTable(TABLE_NAME_1);
        Column column = table.getColumn(COLUMN_NAME_1);

        assertEquals(table, column.table);
        assertEquals(COLUMN_NAME_1, column.name);
        assertEquals(FLOATING, column.type);
    }

    @Test
    public void builder_no_name() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Column.Builder()
                    .type(TEXT)
                    .build(TABLE_1_1));

        // Verify
        assertTrue(thrown.getMessage().contains("no name specified"));
    }

    @Test
    public void builder_no_type() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Column.Builder()
                    .name(COLUMN_NAME_1)
                    .build(TABLE_1_1));

        // Verify
        assertTrue(thrown.getMessage().contains("no type specified"));
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(COLUMN_1_1, COLUMN_3_1);
        Asserts.assertEquality(COLUMN_1_2, COLUMN_3_2);
        Asserts.assertEquality(COLUMN_1_3, COLUMN_3_3);

        Asserts.assertInequality(null,
                COLUMN_1_1,
                COLUMN_1_2,
                COLUMN_1_3,
                COLUMN_2_1,
                COLUMN_2_2,
                COLUMN_2_3);
    }
}
