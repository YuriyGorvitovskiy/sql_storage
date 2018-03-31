package org.eventsourcing.sql_storage.mapping;

import org.eventsourcing.sql_storage.model.Attribute;
import org.eventsourcing.sql_storage.model.EntityType;
import org.eventsourcing.sql_storage.model.Model;
import org.eventsourcing.sql_storage.model.Primitive;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Table;

public class Generator {

    public Schema generate(Model model) {
        Schema.Builder builder = new Schema.Builder();
        for (EntityType type : model.entityTypes.values()) {
            generateTable(builder, type);
        }
        return builder.build();
    }

    public void generateTable(Schema.Builder schema, EntityType type) {
        schema.table(type.name, table -> {
            for (Attribute attribute : type.attributes.values()) {
                generateColumn(table, attribute);
            }
        });
    }

    public void generateColumn(Table.Builder table, Attribute attribute) {
        table.column(attribute.name, convert(attribute.type.primitive));
    }

    public DataType convert(Primitive primitive) {
        if (null != primitive) {
            switch (primitive) {
                case BOOLEAN:
                    return DataType.BOOLEAN;
                case DATETIME:
                    return DataType.DATETIME;
                case FLOATING:
                    return DataType.FLOATING;
                case INTEGER:
                    return DataType.INTEGER;
                case REFERENCE:
                    return DataType.INTEGER;
                case STRING:
                    return DataType.VARCHAR;
                case TEXT:
                    return DataType.TEXT;
            }
        }
        return null;
    }
}
