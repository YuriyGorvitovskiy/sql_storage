package org.eventsourcing.sql_storage.schema;

import static org.eventsourcing.sql_storage.schema.DataType.FLOATING;
import static org.eventsourcing.sql_storage.schema.DataType.INTEGER;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.COLUMN_NAME_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_1_1;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_1_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_2_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_2_3;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_3_1;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_3_2;
import static org.eventsourcing.sql_storage.schema.Example.INDEX_NAME_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_1_1;
import static org.eventsourcing.sql_storage.schema.Example.TABLE_NAME_1;
import static org.junit.Assert.assertEquals;

import org.eventsourcing.sql_storage.test.Asserts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Index_UnitTest {

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
        Column column1 = table.getColumn(COLUMN_NAME_1);
        Column column2 = table.getColumn(COLUMN_NAME_2);
        Index index = table.getIndex(INDEX_NAME_1);

        assertEquals(table, index.owner);
        assertEquals(INDEX_NAME_1, index.name);
        assertEquals(column1, index.columns.get(0));
        assertEquals(column2, index.columns.get(1));
        assertEquals(2, index.columns.size());
    }

    @Test
    public void builder_no_name() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no name specified");

        // Execute
        new Index.Builder()
            .column(COLUMN_NAME_1)
            .build(TABLE_1_1);
    }

    @Test
    public void builder_no_type() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no columns specified");

        // Execute
        new Index.Builder()
            .name(INDEX_NAME_1)
            .build(TABLE_1_1);
    }

    @Test
    public void builder_duplicate_column() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("duplicate column");

        // Execute
        new Index.Builder()
            .name(INDEX_NAME_1)
            .column(COLUMN_NAME_1)
            .column(COLUMN_NAME_2)
            .column(COLUMN_NAME_1)
            .build(TABLE_1_1);
    }

    @Test
    public void builder_no_column_in_table() {
        // Rule
        exception.expect(RuntimeException.class);
        exception.expectMessage("no column");

        // Execute
        new Index.Builder()
            .name(INDEX_NAME_1)
            .column(COLUMN_NAME_1)
            .column("Unknown")
            .build(TABLE_1_1);
    }

    @Test
    public void equals_and_hash() {
        // Execute & Verify
        Asserts.assertEquality(INDEX_1_1, INDEX_3_1);
        Asserts.assertEquality(INDEX_1_2, INDEX_3_2);

        Asserts.assertInequality(null, INDEX_1_1, INDEX_1_2, INDEX_2_2, INDEX_2_3);
    }
}
