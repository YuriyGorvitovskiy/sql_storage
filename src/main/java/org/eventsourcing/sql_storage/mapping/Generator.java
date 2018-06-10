package org.eventsourcing.sql_storage.mapping;

import org.eventsourcing.sql_storage.model.Attribute;
import org.eventsourcing.sql_storage.model.Container;
import org.eventsourcing.sql_storage.model.EntityType;
import org.eventsourcing.sql_storage.model.Model;
import org.eventsourcing.sql_storage.model.Primitive;
import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Schema.Builder;
import org.eventsourcing.sql_storage.schema.Table;

public class Generator {

    public static final String NAME_SEPARATOR         = "_";
    public static final String INDEX_PRIMARY_PREFIX   = "ixp_";
    public static final String INDEX_REFERENCE_PREFIX = "ixr_";

    public Schema generate(Model model) {
        Schema.Builder builder = new Schema.Builder();
        for (EntityType type : model.entityTypes.values()) {
            generateTable(builder, type);
        }
        return builder.build();
    }

    public void generateTable(Schema.Builder schema, EntityType type) {
        String name = type.name;
        schema.table(type.name, table -> {
            table.index(INDEX_PRIMARY_PREFIX + name, Column.ID);
            for (Attribute attribute : type.attributes.values()) {
                generateSingleColumn(table, attribute, name);
            }
        });
        for (Attribute attribute : type.attributes.values()) {
            generateListTable(schema, attribute, name);
            generateMapTable(schema, attribute, name);
        }
    }

    public void generateSingleColumn(Table.Builder table, Attribute attribute, String entityName) {
        if (Container.SINGLE != attribute.type.container)
            return;

        String name = attribute.name;
        table.column(name, convert(attribute.type.primitive));
        generateAttributeIndex(table, attribute, entityName, name);
    }

    public void generateListTable(Builder schema, Attribute attribute, String entityName) {
        if (Container.LIST != attribute.type.container)
            return;

        String name = entityName + NAME_SEPARATOR + attribute.name;
        schema.table(name, table -> {
            table.column(Column.ID, convert(Primitive.REFERENCE))
                .column(Column.VALUE, convert(attribute.type.primitive))
                .index(INDEX_PRIMARY_PREFIX + name, Column.ID);
            generateAttributeIndex(table, attribute, entityName, Column.VALUE);
        });
    }

    public void generateMapTable(Builder schema, Attribute attribute, String entityName) {
        if (Container.MAP != attribute.type.container)
            return;

        String name = entityName + NAME_SEPARATOR + attribute.name;
        schema.table(name, table -> {
            table.column(Column.ID, convert(Primitive.REFERENCE))
                .column(Column.KEY, convert(Primitive.STRING))
                .column(Column.VALUE, convert(attribute.type.primitive))
                .index(INDEX_PRIMARY_PREFIX + name, Column.ID, Column.KEY);
            generateAttributeIndex(table, attribute, entityName, Column.VALUE);
        });
    }

    public void generateAttributeIndex(Table.Builder table, Attribute attribute, String entityName, String columnName) {
        if (Primitive.REFERENCE != attribute.type.primitive)
            return;
        if (Attribute.ID.equals(attribute.name))
            return;

        String name = INDEX_REFERENCE_PREFIX + entityName + NAME_SEPARATOR + attribute.name;
        table.index(name, columnName);
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
