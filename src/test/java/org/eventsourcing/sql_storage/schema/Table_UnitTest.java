package org.eventsourcing.sql_storage.schema;

import static org.eventsourcing.sql_storage.schema.DataType.BOOLEAN;
import static org.eventsourcing.sql_storage.schema.DataType.FLOATING;
import static org.eventsourcing.sql_storage.schema.DataType.INTEGER;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_1_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_1_2;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_1_3;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_2_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_2_2;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_2_3;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_3_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_3_2;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_3_3;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_NAME_1;
import static org.junit.Assert.assertEquals;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Table_UnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void builder_getters() {
        // Execute
        Schema schema = new Schema.Builder()
            .table(TABLE_NAME_1, t -> t
                .column(COLUMN_NAME_1, INTEGER)
                .column(COLUMN_NAME_2, FLOATING)
                .index(INDEX_NAME_1, COLUMN_NAME_1, COLUMN_NAME_2))
            .build();

        // Verify
        Table table = schema.getTable(TABLE_NAME_1);
        assertEquals(TABLE_NAME_1, table.name);

        assertEquals(COLUMN_NAME_1, table.getColumn(COLUMN_NAME_1).name);
        assertEquals(COLUMN_NAME_2, table.getColumn(COLUMN_NAME_2).name);
        assertEquals(2, table.columns.size());

        assertEquals(INDEX_NAME_1, table.getIndex(INDEX_NAME_1).name);
        assertEquals(1, table.indexes.size());
    }

    @Test
    public void builder_no_name() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no name specified");

        // Execute
        new Table.Builder()
            .column(COLUMN_NAME_1, INTEGER)
            .build();
    }

    @Test
    public void builder_no_column() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no columns specified");

        // Execute
        new Table.Builder()
            .name(TABLE_NAME_1)
            .build();
    }

    @Test
    public void builder_duplicate_column() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("duplicate column");

        // Execute
        new Table.Builder()
            .name(TABLE_NAME_1)
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_1, FLOATING)
            .build();
    }

    @Test
    public void builder_duplicate_index() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("duplicate index");

        // Execute
        new Table.Builder()
            .name(TABLE_NAME_1)
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .index(INDEX_NAME_1, COLUMN_NAME_1)
            .index(INDEX_NAME_1, COLUMN_NAME_2)
            .build();
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(TABLE_1_1, TABLE_3_1);
        Asserts.assertEquality(TABLE_1_2, TABLE_3_2);
        Asserts.assertEquality(TABLE_1_3, TABLE_3_3);

        Asserts.assertInequality(null, TABLE_1_1, TABLE_1_2, TABLE_1_3, TABLE_2_1, TABLE_2_2, TABLE_2_3);
    }
}
