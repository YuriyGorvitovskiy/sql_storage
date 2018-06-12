package org.eventsourcing.sql_storage.mapping;

import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Table;
import org.junit.Test;

public class MappingValue_UnitTest {

    @Test
    public void constructor() {
        // Setup
        final String TABLE_NAME = "tbl";
        final String COLUMN_NAME = "col";
        final Schema schema = new Schema.Builder()
            .table(TABLE_NAME, t -> t
                .column(Column.ID, DataType.INTEGER)
                .column(Column.KEY, DataType.VARCHAR)
                .column(COLUMN_NAME, DataType.DATETIME))
            .build();

        final Table table = schema.getTable(TABLE_NAME);
        final Column columnId = table.getColumn(Column.ID);
        final Column columnKey = table.getColumn(Column.KEY);
        final Column columnValue = table.getColumn(COLUMN_NAME);
        final boolean removable = true;

        // Execute
        MappingValue subject = new MappingValue(columnId, columnKey, columnValue, removable);

        // Verify
        assertSame(columnId, subject.columnId);
        assertSame(columnKey, subject.columnKey);
        assertSame(columnValue, subject.columnValue);
        assertSame(removable, subject.removable);
    }
}
