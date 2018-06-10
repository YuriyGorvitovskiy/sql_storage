package org.eventsourcing.sql_storage.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eventsourcing.sql_storage.model.Attribute;
import org.eventsourcing.sql_storage.model.AttributeId;
import org.eventsourcing.sql_storage.model.Container;
import org.eventsourcing.sql_storage.model.EntityType;
import org.eventsourcing.sql_storage.model.Model;
import org.eventsourcing.sql_storage.model.Primitive;
import org.eventsourcing.sql_storage.model.Relation;
import org.eventsourcing.sql_storage.model.ValueType;
import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Schema;
import org.eventsourcing.sql_storage.schema.Schema.Builder;
import org.eventsourcing.sql_storage.schema.Table;
import org.eventsourcing.sql_storage.util.Helper;

public class Generator {

    public static final String NAME_SEPARATOR         = "_";
    public static final String INDEX_PRIMARY_PREFIX   = "ixp_";
    public static final String INDEX_REFERENCE_PREFIX = "ixr_";

    public Schema generate(Model model) {
        Set<AttributeId> primaryRelations = collectPrimaryRelations(model);
        Schema.Builder builder = new Schema.Builder();
        for (EntityType type : model.entityTypes.values()) {
            generateTable(builder, type, primaryRelations);
        }
        return builder.build();
    }

    public void generateTable(Schema.Builder schema, EntityType type, Set<AttributeId> primaryRelations) {
        String name = type.name;
        schema.table(type.name, table -> {
            table.index(INDEX_PRIMARY_PREFIX + name, Column.ID);
            for (Attribute attribute : type.attributes.values()) {
                generateSingleColumn(table, primaryRelations, attribute, name);
                generateSingleReference(table, primaryRelations, attribute, name);
            }
        });
        for (Attribute attribute : type.attributes.values()) {
            generateListTable(schema, primaryRelations, attribute, name);
            generateMapTable(schema, primaryRelations, attribute, name);
        }
    }

    public void generateSingleColumn(Table.Builder table,
            Set<AttributeId> primary,
            Attribute attribute,
            String entityName) {

        if (Container.SINGLE != attribute.type.container)
            return;

        if (Primitive.REFERENCE == attribute.type.primitive)
            return;

        String name = attribute.name;
        table.column(name, convert(attribute.type.primitive));
        generateAttributeIndex(table, attribute, entityName, name);
    }

    public void generateSingleReference(Table.Builder table,
            Set<AttributeId> primary,
            Attribute attribute,
            String entityName) {

        if (!ValueType.REFERENCE.equals(attribute.type))
            return;

        if (!primary.contains(new AttributeId(attribute)))
            return;

        String name = attribute.name;
        table.column(name, convert(attribute.type.primitive));

        Relation relation = Helper.anyValue(attribute.relations);
        if (null != relation && Container.MAP == relation.reverse.type.container) {
            String key = name + NAME_SEPARATOR + Column.KEY;
            String index = INDEX_REFERENCE_PREFIX + entityName + NAME_SEPARATOR + attribute.name;
            table
                .column(key, convert(Primitive.STRING))
                .index(index, name, key);
        } else {
            generateAttributeIndex(table, attribute, entityName, name);
        }
    }

    public void generateListTable(Builder schema, Set<AttributeId> primary, Attribute attribute, String entityName) {
        if (Container.LIST != attribute.type.container)
            return;
        if (Primitive.REFERENCE == attribute.type.primitive && !primary.contains(new AttributeId(attribute)))
            return;

        String name = entityName + NAME_SEPARATOR + attribute.name;
        schema.table(name, table -> {
            table.column(Column.ID, convert(Primitive.REFERENCE))
                .column(Column.VALUE, convert(attribute.type.primitive))
                .index(INDEX_PRIMARY_PREFIX + name, Column.ID);
            generateAttributeIndex(table, attribute, entityName, Column.VALUE);
        });
    }

    public void generateMapTable(Builder schema, Set<AttributeId> primary, Attribute attribute, String entityName) {
        if (Container.MAP != attribute.type.container)
            return;
        if (Primitive.REFERENCE == attribute.type.primitive && !primary.contains(new AttributeId(attribute)))
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

    Set<AttributeId> collectPrimaryRelations(Model model) {
        List<Attribute> doubtful = new ArrayList<>();
        Set<AttributeId> primary = new HashSet<>();
        for (EntityType type : model.entityTypes.values()) {
            for (Attribute attribute : type.attributes.values()) {
                checkRelation(attribute, doubtful, primary);
            }
        }
        checkDoubtful(doubtful, primary);
        return primary;
    }

    void checkRelation(Attribute attribute, List<Attribute> doubtful, Set<AttributeId> primary) {
        if (Primitive.REFERENCE != attribute.type.primitive)
            return;

        if (attribute.relations.isEmpty()) {
            primary.add(new AttributeId(attribute));
            return;
        }

        final Container container = attribute.type.container;
        final Container reverse = Helper.anyValue(attribute.relations).reverse.type.container;
        if (container == reverse) {
            doubtful.add(attribute);
        } else if (Container.SINGLE == container) {
            primary.add(new AttributeId(attribute));
        } else if (Container.MAP == container && Container.LIST == reverse) {
            primary.add(new AttributeId(attribute));
        }
    }

    void checkDoubtful(List<Attribute> doubtful, Set<AttributeId> primary) {
        Set<AttributeId> processed = new HashSet<>();
        Set<AttributeId> left = new HashSet<>();
        Set<AttributeId> right = new HashSet<>();
        for (Attribute attribute : doubtful) {
            if (!collectAttributes(attribute, processed, left, right))
                continue;

            if (getBest(left).compareTo(getBest(right)) < 0) {
                primary.addAll(left);
            } else {
                primary.addAll(right);
            }
            left.clear();
            right.clear();
        }
    }

    boolean collectAttributes(Attribute attribute,
            Set<AttributeId> processed,
            Set<AttributeId> left,
            Set<AttributeId> right) {
        AttributeId id = new AttributeId(attribute);
        if (processed.contains(id))
            return false;

        left.add(id);
        processed.add(id);
        for (Relation relation : attribute.relations.values()) {
            collectAttributes(relation.reverse, processed, right, left);
        }
        return true;
    }

    AttributeId getBest(Collection<AttributeId> list) {
        AttributeId best = null;
        for (AttributeId id : list) {
            if (null == best || best.compareTo(id) > 0) {
                best = id;
            }
        }
        return best;
    }
}
