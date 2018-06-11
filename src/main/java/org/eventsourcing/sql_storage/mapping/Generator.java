package org.eventsourcing.sql_storage.mapping;

import org.eventsourcing.sql_storage.model.Attribute;
import org.eventsourcing.sql_storage.model.Container;
import org.eventsourcing.sql_storage.model.EntityType;
import org.eventsourcing.sql_storage.model.Model;
import org.eventsourcing.sql_storage.model.Primitive;
import org.eventsourcing.sql_storage.model.Relation;
import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Table;

public class Generator {

    public static final String NAME_SEPARATOR       = "_";
    public static final String PRIMARY_KEY_PREFIX   = "pk_";
    public static final String PRIMARY_INDEX_PREFIX = "px_";
    public static final String INDEX_VALUE_PREFIX   = "ix_";

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
            table.primaryKey(PRIMARY_KEY_PREFIX + name, Column.ID);
            for (Attribute attribute : type.attributes.values()) {
                if (Container.SINGLE == attribute.type.container) {
                    generateSingleAttribute(table, attribute, name);
                }
            }
        });
        for (Attribute attribute : type.attributes.values()) {
            if (Container.LIST == attribute.type.container) {
                generateListAttribute(schema, attribute, name);
            } else if (Container.MAP == attribute.type.container) {
                generateMapAttribute(schema, attribute, name);
            }
        }
    }

    public void generateSingleAttribute(Table.Builder table, Attribute attribute, String entityName) {
        if (attribute.relations.isEmpty()) {
            String name = attribute.name;
            table.column(name, convert(attribute.type.primitive));
            return;
        }
        for (Relation relation : attribute.relations.values()) {
            generateSingleRelation(table, attribute, relation, entityName);
        }
    }

    public void generateListAttribute(Schema.Builder schema, Attribute attribute, String entityName) {
        if (attribute.relations.isEmpty()) {
            String name = entityName + NAME_SEPARATOR + attribute.name;
            schema.table(name, t -> t
                .column(Column.ID, convert(Primitive.REFERENCE))
                .column(Column.VALUE, convert(attribute.type.primitive))
                .index(PRIMARY_INDEX_PREFIX + name, Column.ID));
            return;
        }
        for (Relation relation : attribute.relations.values()) {
            generateListRelation(schema, attribute, relation, entityName);
        }
    }

    public void generateMapAttribute(Schema.Builder schema, Attribute attribute, String entityName) {
        if (attribute.relations.isEmpty()) {
            String name = entityName + NAME_SEPARATOR + attribute.name;
            schema.table(name, t -> t
                .column(Column.ID, convert(Primitive.REFERENCE))
                .column(Column.KEY, convert(Primitive.STRING))
                .column(Column.VALUE, convert(attribute.type.primitive))
                .primaryKey(PRIMARY_KEY_PREFIX + name, Column.ID, Column.KEY));
            return;
        }

        for (Relation relation : attribute.relations.values()) {
            generateMapRelation(schema, attribute, relation, entityName);
        }
    }

    public void generateSingleRelation(Table.Builder table, Attribute attribute, Relation relation, String entityName) {
        if (Container.SINGLE == relation.reverse.type.container && !isBest(attribute, relation))
            return;

        String name = attribute.name + NAME_SEPARATOR + relation.reverse.owner.typeId;

        if (Container.MAP == relation.reverse.type.container) {
            String key = name + NAME_SEPARATOR + Column.KEY;
            table
                .column(name, convert(attribute.type.primitive))
                .column(key, convert(Primitive.STRING))
                .index(INDEX_VALUE_PREFIX + entityName + NAME_SEPARATOR + name, name, key);
            return;
        }

        table
            .column(name, convert(attribute.type.primitive))
            .index(INDEX_VALUE_PREFIX + entityName + NAME_SEPARATOR + name, name);
    }

    public void generateListRelation(Schema.Builder schema, Attribute attribute, Relation relation, String entityName) {
        if (Container.LIST != relation.reverse.type.container)
            return;

        if (!isBest(attribute, relation))
            return;

        String name = entityName + NAME_SEPARATOR + attribute.name + NAME_SEPARATOR + relation.reverse.owner.typeId;
        String column = relation.reverse.owner.name + NAME_SEPARATOR + Column.ID;
        schema.table(name, t -> t
            .column(Column.ID, convert(Primitive.REFERENCE))
            .column(column, convert(attribute.type.primitive))
            .index(PRIMARY_INDEX_PREFIX + name, Column.ID)
            .index(INDEX_VALUE_PREFIX + name, column));
    }

    public void generateMapRelation(Schema.Builder schema, Attribute attribute, Relation relation, String entityName) {
        if (Container.SINGLE == relation.reverse.type.container)
            return;

        if (Container.MAP == relation.reverse.type.container && !isBest(attribute, relation))
            return;

        String name = entityName + NAME_SEPARATOR + attribute.name + NAME_SEPARATOR + relation.reverse.owner.typeId;
        String column = relation.reverse.owner.name + NAME_SEPARATOR + Column.ID;
        schema.table(name, t -> t
            .column(Column.ID, convert(Primitive.REFERENCE))
            .column(Column.KEY, convert(Primitive.STRING))
            .column(column, convert(attribute.type.primitive))
            .primaryKey(PRIMARY_KEY_PREFIX + name, Column.ID, Column.KEY)
            .index(INDEX_VALUE_PREFIX + name, column));
    }

    boolean isBest(Attribute attribute, Relation relation) {
        if (attribute.owner.typeId == relation.reverse.owner.typeId)
            return attribute.name.compareTo(relation.reverse.name) < 0;

        return attribute.owner.typeId < relation.reverse.owner.typeId;
    }

    DataType convert(Primitive primitive) {
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
