package org.eventsourcing.sql_storage.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Model {

    public static class Builder {

        List<Consumer<EntityType.Builder>> typeDefiners = new ArrayList<>();

        public Builder() {
        }

        public Builder type(Consumer<EntityType.Builder> typeDefiner) {
            this.typeDefiners.add(typeDefiner);
            return this;
        }

        public Builder type(long typeId, String name, Consumer<EntityType.Builder> typeDefiner) {
            return type((t) -> typeDefiner.accept(t.typeId(typeId).name(name)));
        }

        public Model build() {
            Map<Long, EntityType> typeIdMap = new HashMap<>();
            Map<String, EntityType> typeNameMap = new HashMap<>();
            Model model = new Model(typeNameMap);

            // first pass
            List<Consumer<Model>> resolvers = new ArrayList<>();
            for (Consumer<EntityType.Builder> typeDefiner : typeDefiners) {
                EntityType.Builder builder = new EntityType.Builder();
                typeDefiner.accept(builder);

                EntityType entityType = builder.build(model, resolvers);
                EntityType duplicate = typeIdMap.put(entityType.typeId, entityType);
                if (null != duplicate)
                    throw new RuntimeException(
                        "Model has duplicate Entity Type Ids: " + duplicate + " & " + entityType);

                duplicate = typeNameMap.put(entityType.name, entityType);
                if (null != duplicate)
                    throw new RuntimeException(
                        "Model has duplicate Entity Type names: " + duplicate + " & " + entityType);
            }

            // second pass
            for (Consumer<Model> resolver : resolvers) {
                resolver.accept(model);
            }

            // third validation pass
            Set<AttributeId> processed = new HashSet<AttributeId>();
            for (EntityType type : model.entityTypes.values()) {
                for (Attribute attr : type.attributes.values()) {
                    for (Relation relation : attr.relations.values()) {
                        checkReverseRelation(attr, relation);
                    }
                    checkReverseContainerTypes(attr);
                    checkCombineRelationConsistency(attr, processed);
                }
            }
            return model;
        }

        private void checkReverseRelation(Attribute attr, Relation relation) {
            for (Relation reverseRelation : relation.reverse.relations.values()) {
                if (reverseRelation.reverse == attr)
                    return;
            }
            throw new RuntimeException(
                attr + " has inconsistent relation " + relation);
        }

        private void checkReverseContainerTypes(Attribute attr) {
            Container type = null;
            Relation first = null;
            for (Relation relation : attr.relations.values()) {
                if (null == type) {
                    type = relation.reverse.type.container;
                    first = relation;
                } else if (type != relation.reverse.type.container) {
                    throw new RuntimeException(
                        attr + " has different container type between reverse relations: "
                                + first + " and " + relation);
                }
            }
        }

        private void checkCombineRelationConsistency(Attribute attr, Set<AttributeId> processed) {
            if (attr.relations.isEmpty())
                return;

            AttributeId id = new AttributeId(attr);
            if (!processed.add(id))
                return;

            Set<AttributeId> left = new HashSet<>();
            Set<AttributeId> right = new HashSet<>();
            left.add(id);
            fillCombineRelation(attr.relations.values(), left, right, processed);
            left.retainAll(right);
            if (left.isEmpty())
                return;

            throw new RuntimeException(
                "The following attribute(s) are present on both sides of the combined relation: " + left);

        }

        private void fillCombineRelation(Collection<Relation> relations,
                Set<AttributeId> left,
                Set<AttributeId> right,
                Set<AttributeId> processed) {
            for (Relation relation : relations) {
                AttributeId id = new AttributeId(relation.reverse);
                right.add(id);
                if (!processed.add(id))
                    continue;
                fillCombineRelation(relation.reverse.relations.values(), right, left, processed);
            }
        }

    }

    public final Map<String, EntityType> entityTypes;

    public Model(Map<String, EntityType> entityTypeMap) {
        this.entityTypes = Collections.unmodifiableMap(entityTypeMap);
    }

    public EntityType getEntityType(String entityTypeName) {
        return entityTypes.get(entityTypeName);
    }

    public Attribute getAttribute(String entityTypeName, String attributeName) {
        EntityType type = entityTypes.get(entityTypeName);
        if (null == type)
            return null;
        return type.attributes.get(attributeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Model))
            return false;

        Model other = (Model) obj;
        return Objects.equals(this.entityTypes, other.entityTypes);
    }

    @Override
    public String toString() {
        return "Model@" + Integer.toHexString(super.hashCode()) + " [entityTypes=" + entityTypes.values() + "]";
    }
}
