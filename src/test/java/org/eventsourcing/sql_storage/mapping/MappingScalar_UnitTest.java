package org.eventsourcing.sql_storage.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.eventsourcing.sql_storage.data.Ref;
import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Table;
import org.junit.Test;

public class MappingScalar_UnitTest {

    @Test
    public void constructor_getters() {
        // Setup
        final long TYPE_ID = 1;

        final String TABLE_NAME = "tbl";
        final String COLUMN1_NAME = "col";
        final Schema schema = new Schema.Builder()
            .table(TABLE_NAME, t -> t
                .column(Column.ID, DataType.INTEGER)
                .column(Column.KEY, DataType.VARCHAR)
                .column(COLUMN1_NAME, DataType.INTEGER))
            .build();

        final Table table = schema.getTable(TABLE_NAME);
        final MappingValue value = new MappingValue(
            table.getColumn(Column.ID),
            table.getColumn(Column.KEY),
            table.getColumn(COLUMN1_NAME),
            true);

        // Execute
        MappingScalar subject = new MappingScalar(value);

        // Verify
        assertEquals(value, subject.value);
        assertSame(value, subject.get(new Ref(TYPE_ID, 1234L)));
        assertSame(value, subject.get(null));
    }
}
