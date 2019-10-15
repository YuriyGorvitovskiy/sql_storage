package org.eventsourcing.sql_storage.schema;

import static org.eventsourcing.sql_storage.schema.DataType.FLOATING;
import static org.eventsourcing.sql_storage.schema.DataType.INTEGER;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_1_1;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_1_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_2_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_2_3;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_2_3_1;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_3_1;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_3_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_1_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_NAME_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Index_UnitTest {

    @Test
    public void builder_getters() {
        // Execute
        Schema schema = new Schema.Builder()
            .table(TABLE_NAME_1,
                    t -> t
                        .column(COLUMN_NAME_1, INTEGER)
                        .column(COLUMN_NAME_2, FLOATING)
                        .index(INDEX_NAME_1, COLUMN_NAME_1, COLUMN_NAME_2))
            .build();

        // Verify
        Table  table   = schema.getTable(TABLE_NAME_1);
        Column column1 = table.getColumn(COLUMN_NAME_1);
        Column column2 = table.getColumn(COLUMN_NAME_2);
        Index  index   = table.getIndex(INDEX_NAME_1);

        assertEquals(table, index.table);
        assertEquals(INDEX_NAME_1, index.name);
        assertEquals(column1, index.columns.get(0));
        assertEquals(column2, index.columns.get(1));
        assertEquals(2, index.columns.size());
        assertEquals(false, index.primary);
    }

    @Test
    public void builder_primary() {
        // Execute
        Schema schema = new Schema.Builder()
            .table(TABLE_NAME_1,
                    t -> t
                        .column(COLUMN_NAME_1, INTEGER)
                        .column(COLUMN_NAME_2, FLOATING)
                        .primaryKey(INDEX_NAME_1, COLUMN_NAME_1, COLUMN_NAME_2))
            .build();

        // Verify
        Table  table   = schema.getTable(TABLE_NAME_1);
        Column column1 = table.getColumn(COLUMN_NAME_1);
        Column column2 = table.getColumn(COLUMN_NAME_2);
        Index  index   = table.getIndex(INDEX_NAME_1);

        assertEquals(table, index.table);
        assertEquals(INDEX_NAME_1, index.name);
        assertEquals(column1, index.columns.get(0));
        assertEquals(column2, index.columns.get(1));
        assertEquals(2, index.columns.size());
        assertEquals(true, index.primary);
    }

    @Test
    public void builder_no_name() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Index.Builder()
                    .column(COLUMN_NAME_1)
                    .build(TABLE_1_1));

        // Verify
        assertTrue(thrown.getMessage().contains("no name specified"));
    }

    @Test
    public void builder_no_type() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Index.Builder()
                    .name(INDEX_NAME_1)
                    .build(TABLE_1_1));

        // Verify
        assertTrue(thrown.getMessage().contains("no columns specified"));
    }

    @Test
    public void builder_duplicate_column() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Index.Builder()
                    .name(INDEX_NAME_1)
                    .column(COLUMN_NAME_1)
                    .column(COLUMN_NAME_2)
                    .column(COLUMN_NAME_1)
                    .build(TABLE_1_1));

        // Verify
        assertTrue(thrown.getMessage().contains("duplicate column"));
    }

    @Test
    public void builder_no_column_in_table() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new Index.Builder()
                    .name(INDEX_NAME_1)
                    .column(COLUMN_NAME_1)
                    .column("Unknown")
                    .build(TABLE_1_1));

        // Verify
        assertTrue(thrown.getMessage().contains("no column"));
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(INDEX_1_1, INDEX_3_1);
        Asserts.assertEquality(INDEX_1_2, INDEX_3_2);

        Asserts.assertInequality(null, INDEX_1_1, INDEX_1_2, INDEX_2_2, INDEX_2_3, INDEX_2_3_1);

    }
}
