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
    public void generate() {
        // Setup
        Model model = new Model.Builder()
            .type(1, "type_single", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN)
                .attribute("attr_datetime", ValueType.DATETIME)
                .attribute("attr_floating", ValueType.FLOATING)
                .attribute("attr_integer", ValueType.INTEGER)
                .attribute("attr_reference", ValueType.REFERENCE, a -> a
                    .relation("type_list", "attr_reference"))
                .attribute("attr_string", ValueType.STRING)
                .attribute("attr_text", ValueType.TEXT))
            .type(2, "type_list", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN_LIST)
                .attribute("attr_datetime", ValueType.DATETIME_LIST)
                .attribute("attr_floating", ValueType.FLOATING_LIST)
                .attribute("attr_integer", ValueType.INTEGER_LIST)
                .attribute("attr_reference", ValueType.REFERENCE_LIST, a -> a
                    .relation("type_single", "attr_reference")
                    .relation("type_map", "attr_reference"))
                .attribute("attr_string", ValueType.STRING_LIST)
                .attribute("attr_text", ValueType.TEXT_LIST))
            .type(3, "type_map", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN_MAP)
                .attribute("attr_datetime", ValueType.DATETIME_MAP)
                .attribute("attr_floating", ValueType.FLOATING_MAP)
                .attribute("attr_integer", ValueType.INTEGER_MAP)
                .attribute("attr_reference", ValueType.REFERENCE_MAP, a -> a
                    .relation("type_list", "attr_reference"))
                .attribute("attr_string", ValueType.STRING_MAP)
                .attribute("attr_text", ValueType.TEXT_MAP))
            .build();

        Schema expected = new Schema.Builder()
            .table("type_single", t -> t
                .column("id", DataType.INTEGER)
                .column("attr_boolean", DataType.BOOLEAN)
                .column("attr_datetime", DataType.DATETIME)
                .column("attr_floating", DataType.FLOATING)
                .column("attr_integer", DataType.INTEGER)
                .column("attr_reference", DataType.INTEGER)
                .column("attr_string", DataType.VARCHAR)
                .column("attr_text", DataType.TEXT)
                .index("ixp_type_single", "id")
                .index("ixr_type_single_attr_reference", "attr_reference"))

            .table("type_list", t -> t
                .column("id", DataType.INTEGER)
                .index("ixp_type_list", "id"))
            .table("type_list_attr_boolean", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.BOOLEAN)
                .index("ixp_type_list_attr_boolean", "id"))
            .table("type_list_attr_datetime", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.DATETIME)
                .index("ixp_type_list_attr_datetime", "id"))
            .table("type_list_attr_floating", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.FLOATING)
                .index("ixp_type_list_attr_floating", "id"))
            .table("type_list_attr_integer", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.INTEGER)
                .index("ixp_type_list_attr_integer", "id"))
            .table("type_list_attr_reference", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.INTEGER)
                .index("ixp_type_list_attr_reference", "id")
                .index("ixr_type_list_attr_reference", "value"))
            .table("type_list_attr_string", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.VARCHAR)
                .index("ixp_type_list_attr_string", "id"))
            .table("type_list_attr_text", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.TEXT)
                .index("ixp_type_list_attr_text", "id"))

            .table("type_map", t -> t
                .column("id", DataType.INTEGER)
                .index("ixp_type_map", "id"))
            .table("type_map_attr_boolean", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.BOOLEAN)
                .index("ixp_type_map_attr_boolean", "id", "key"))
            .table("type_map_attr_datetime", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.DATETIME)
                .index("ixp_type_map_attr_datetime", "id", "key"))
            .table("type_map_attr_floating", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.FLOATING)
                .index("ixp_type_map_attr_floating", "id", "key"))
            .table("type_map_attr_integer", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.INTEGER)
                .index("ixp_type_map_attr_integer", "id", "key"))
            .table("type_map_attr_reference", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.INTEGER)
                .index("ixp_type_map_attr_reference", "id", "key")
                .index("ixr_type_map_attr_reference", "value"))
            .table("type_map_attr_string", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.VARCHAR)
                .index("ixp_type_map_attr_string", "id", "key"))
            .table("type_map_attr_text", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.TEXT)
                .index("ixp_type_map_attr_text", "id", "key"))
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
