package org.eventsourcing.sql_storage.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.eventsourcing.sql_storage.data.Ref;
import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Table;
import org.junit.Test;

public class MappingRelation_UnitTest {

    @Test
    public void constructor_getters() {
        // Setup
        final long TYPE_ID1 = 1;
        final long TYPE_ID2 = 2;

        final String TABLE_NAME = "tbl";
        final String COLUMN1_NAME = "col_1";
        final String COLUMN2_NAME = "col_2";
        final Schema schema = new Schema.Builder()
            .table(TABLE_NAME, t -> t
                .column(Column.ID, DataType.INTEGER)
                .column(Column.KEY, DataType.VARCHAR)
                .column(COLUMN1_NAME, DataType.INTEGER)
                .column(COLUMN2_NAME, DataType.INTEGER))
            .build();

        final Table table = schema.getTable(TABLE_NAME);
        final MappingValue value1 = new MappingValue(
            table.getColumn(Column.ID),
            table.getColumn(Column.KEY),
            table.getColumn(COLUMN1_NAME),
            true);
        final MappingValue value2 = new MappingValue(
            table.getColumn(Column.ID),
            table.getColumn(Column.KEY),
            table.getColumn(COLUMN2_NAME),
            true);

        Map<Long, MappingValue> mapping = new HashMap<>();
        mapping.put(TYPE_ID1, value1);
        mapping.put(TYPE_ID2, value2);

        // Execute
        MappingRelation subject = new MappingRelation(mapping);

        // Verify
        assertEquals(mapping, subject.relations);
        assertSame(value1, subject.get(new Ref(TYPE_ID1, 1234L)));
        assertSame(value2, subject.get(new Ref(TYPE_ID2, 1234L)));
    }
}
