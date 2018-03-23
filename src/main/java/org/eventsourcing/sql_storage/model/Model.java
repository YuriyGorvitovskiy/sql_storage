package org.eventsourcing.sql_storage.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            for (EntityType type : model.entityTypes.values()) {
                for (Attribute attr : type.attributes.values()) {
                    for (Relation relation : attr.relations.values()) {
                        checkReverseRelation(attr, relation);
                    }
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

    }

    public final Map<String, EntityType> entityTypes;

    public Model(Map<String, EntityType> entityTypeMap) {
        this.entityTypes = Collections.unmodifiableMap(entityTypeMap);
    }

    public EntityType getEntityType(String entityTypeName) {
        return entityTypes.get(entityTypeName);
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
