package org.eventsourcing.sql_storage.mapping;

import static org.junit.Assert.assertEquals;

import org.eventsourcing.sql_storage.model.Model;
import org.eventsourcing.sql_storage.model.Primitive;
import org.eventsourcing.sql_storage.model.ValueType;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.junit.Test;

public class Generator_UnitTest {

    @Test
    public void test() {
        // Setup
        Model model = new Model.Builder()
            .type(1, "type_a", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN)
                .attribute("attr_datetime", ValueType.DATETIME)
                .attribute("attr_floating", ValueType.FLOATING)
                .attribute("attr_integer", ValueType.INTEGER)
                .attribute("attr_string", ValueType.STRING)
                .attribute("attr_text", ValueType.TEXT))
            .build();

        Schema expected = new Schema.Builder()
            .table("type_a", t -> t
                .column("id", DataType.INTEGER)
                .column("attr_boolean", DataType.BOOLEAN)
                .column("attr_datetime", DataType.DATETIME)
                .column("attr_floating", DataType.FLOATING)
                .column("attr_integer", DataType.INTEGER)
                .column("attr_string", DataType.VARCHAR)
                .column("attr_text", DataType.TEXT))
            .build();

        // Execute
        Schema actual = new Generator().generate(model);

        // Validate
        assertEquals(expected, actual);
    }

    @Test
    public void convert() {
        // Setup
        Generator subject = new Generator();

        // Execute & Validate
        assertEquals(null, subject.convert(null));
        assertEquals(DataType.BOOLEAN, subject.convert(Primitive.BOOLEAN));
        assertEquals(DataType.DATETIME, subject.convert(Primitive.DATETIME));
        assertEquals(DataType.FLOATING, subject.convert(Primitive.FLOATING));
        assertEquals(DataType.INTEGER, subject.convert(Primitive.INTEGER));
        assertEquals(DataType.INTEGER, subject.convert(Primitive.REFERENCE));
        assertEquals(DataType.VARCHAR, subject.convert(Primitive.STRING));
        assertEquals(DataType.TEXT, subject.convert(Primitive.TEXT));
    }

}
