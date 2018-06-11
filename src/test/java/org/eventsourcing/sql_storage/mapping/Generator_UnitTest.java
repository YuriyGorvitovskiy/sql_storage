package org.eventsourcing.sql_storage.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;

import org.eventsourcing.sql_storage.model.AttributeId;
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
                .attribute("ref_list", ValueType.REFERENCE, "type_list", "ref_single")
                .attribute("ref_map", ValueType.REFERENCE, "type_map", "ref_single")
                .attribute("ref_single", ValueType.REFERENCE, "type_single", "ref_single_back")
                .attribute("ref_single_back", ValueType.REFERENCE, "type_single", "ref_single")
                .attribute("attr_string", ValueType.STRING)
                .attribute("attr_text", ValueType.TEXT))
            .type(2, "type_list", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN_LIST)
                .attribute("attr_datetime", ValueType.DATETIME_LIST)
                .attribute("attr_floating", ValueType.FLOATING_LIST)
                .attribute("attr_integer", ValueType.INTEGER_LIST)
                .attribute("ref_list", ValueType.REFERENCE_LIST, "type_list", "ref_list_back")
                .attribute("ref_list_back", ValueType.REFERENCE_LIST, "type_list", "ref_list")
                .attribute("ref_map", ValueType.REFERENCE_LIST, "type_map", "ref_list")
                .attribute("ref_single", ValueType.REFERENCE_LIST, "type_single", "ref_list")
                .attribute("attr_string", ValueType.STRING_LIST)
                .attribute("attr_text", ValueType.TEXT_LIST))
            .type(3, "type_map", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN_MAP)
                .attribute("attr_datetime", ValueType.DATETIME_MAP)
                .attribute("attr_floating", ValueType.FLOATING_MAP)
                .attribute("attr_integer", ValueType.INTEGER_MAP)
                .attribute("ref_list", ValueType.REFERENCE_MAP, "type_list", "ref_map")
                .attribute("ref_map", ValueType.REFERENCE_MAP, "type_map", "ref_map_back")
                .attribute("ref_map_back", ValueType.REFERENCE_MAP, "type_map", "ref_map")
                .attribute("ref_single", ValueType.REFERENCE_MAP, "type_single", "ref_map")
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
                .column("ref_list", DataType.INTEGER)
                .column("ref_map", DataType.INTEGER)
                .column("ref_map_key", DataType.VARCHAR)
                .column("ref_single", DataType.INTEGER)
                .column("attr_string", DataType.VARCHAR)
                .column("attr_text", DataType.TEXT)
                .primaryKey("pk_type_single", "id")
                .index("ix_type_single_ref_list", "ref_list")
                .index("ix_type_single_ref_map", "ref_map", "ref_map_key")
                .index("ix_type_single_ref_single", "ref_single"))

            .table("type_list", t -> t
                .column("id", DataType.INTEGER)
                .primaryKey("pk_type_list", "id"))
            .table("type_list_attr_boolean", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.BOOLEAN)
                .index("px_type_list_attr_boolean", "id"))
            .table("type_list_attr_datetime", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.DATETIME)
                .index("px_type_list_attr_datetime", "id"))
            .table("type_list_attr_floating", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.FLOATING)
                .index("px_type_list_attr_floating", "id"))
            .table("type_list_attr_integer", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.INTEGER)
                .index("px_type_list_attr_integer", "id"))
            .table("type_list_ref_list", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.INTEGER)
                .index("px_type_list_ref_list", "id")
                .index("ix_type_list_ref_list", "value"))
            .table("type_list_attr_string", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.VARCHAR)
                .index("px_type_list_attr_string", "id"))
            .table("type_list_attr_text", t -> t
                .column("id", DataType.INTEGER)
                .column("value", DataType.TEXT)
                .index("px_type_list_attr_text", "id"))

            .table("type_map", t -> t
                .column("id", DataType.INTEGER)
                .primaryKey("pk_type_map", "id"))
            .table("type_map_attr_boolean", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.BOOLEAN)
                .primaryKey("pk_type_map_attr_boolean", "id", "key"))
            .table("type_map_attr_datetime", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.DATETIME)
                .primaryKey("pk_type_map_attr_datetime", "id", "key"))
            .table("type_map_attr_floating", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.FLOATING)
                .primaryKey("pk_type_map_attr_floating", "id", "key"))
            .table("type_map_attr_integer", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.INTEGER)
                .primaryKey("pk_type_map_attr_integer", "id", "key"))
            .table("type_map_ref_list", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.INTEGER)
                .primaryKey("pk_type_map_ref_list", "id", "key")
                .index("ix_type_map_ref_list", "value"))
            .table("type_map_ref_map", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.INTEGER)
                .primaryKey("pk_type_map_ref_map", "id", "key")
                .index("ix_type_map_ref_map", "value"))
            .table("type_map_attr_string", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.VARCHAR)
                .primaryKey("pk_type_map_attr_string", "id", "key"))
            .table("type_map_attr_text", t -> t
                .column("id", DataType.INTEGER)
                .column("key", DataType.VARCHAR)
                .column("value", DataType.TEXT)
                .primaryKey("pk_type_map_attr_text", "id", "key"))

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

    @Test
    public void getBest() {
        // Setup
        Model model = new Model.Builder()
            .type(1, "type_single", t -> t
                .attribute("attr_boolean", ValueType.BOOLEAN)
                .attribute("attr_datetime", ValueType.DATETIME)
                .attribute("attr_floating", ValueType.FLOATING))
            .build();

        final AttributeId id1 = new AttributeId(model.getAttribute("type_single", "attr_boolean"));
        final AttributeId id2 = new AttributeId(model.getAttribute("type_single", "attr_datetime"));
        final AttributeId id3 = new AttributeId(model.getAttribute("type_single", "attr_floating"));

        final Collection<AttributeId> ids = Arrays.asList(id2, id1, id3);

        // Execute & Verify
        assertSame(id1, new Generator().getBest(ids));

    }
}
