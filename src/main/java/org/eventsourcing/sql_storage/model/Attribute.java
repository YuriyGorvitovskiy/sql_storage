package org.eventsourcing.sql_storage.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.eventsourcing.sql_storage.util.Helper;

public class Attribute {

    public static class Builder {
        String                           name;
        ValueType                        type;
        List<Consumer<Relation.Builder>> relationDefiners = new ArrayList<>();

        Builder() {
        }

        public Builder name(String name) {
            this.name = name.trim();
            return this;
        }

        public Builder type(ValueType type) {
            this.type = type;
            return this;
        }

        public Builder relation(Consumer<Relation.Builder> relationDefiner) {
            relationDefiners.add(relationDefiner);
            return this;
        }

        public Builder relation(String target, String reverse) {
            return relation((r) -> r
                .target(target)
                .reverse(reverse));
        }

        public Attribute build(EntityType entityType, List<Consumer<Model>> resolvers) {
            if (Helper.isEmpty(name))
                throw new RuntimeException("Attribute has no name specified.");

            Map<String, Relation> relationMap = new HashMap<>();
            Attribute attribute = new Attribute(entityType, name, type, relationMap);

            if (Primitive.REFERENCE == type.primitive && relationDefiners.isEmpty())
                throw new RuntimeException(
                    "Attribute " + attribute + " of type " + type + " has no relation defined.");

            if (Primitive.REFERENCE != type.primitive && !relationDefiners.isEmpty())
                throw new RuntimeException(
                    "Attribute " + attribute + " of type " + type + " is not allowed to have relations.");

            for (Consumer<Relation.Builder> relationDefiner : relationDefiners) {
                resolvers.add((m) -> {
                    Relation.Builder builder = new Relation.Builder();
                    relationDefiner.accept(builder);

                    Relation relation = builder.build(m);
                    Relation duplicate = relationMap.put(relation.target.name, relation);
                    if (null != duplicate)
                        throw new RuntimeException(
                            "Attribute " + attribute + " has duplicate target entities: "
                                    + duplicate + " & " + relation);
                });
            }
            return attribute;
        }
    }

    public final EntityType            owner;
    public final String                name;
    public final ValueType             type;
    public final Map<String, Relation> relations;

    Attribute(EntityType owner, String name, ValueType type, Map<String, Relation> relations) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.relations = Collections.unmodifiableMap(relations);
    }

    public Relation getRelation(String target) {
        return relations.get(target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, relations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Attribute))
            return false;

        Attribute other = (Attribute) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.type, other.type)
                && Objects.equals(this.relations, other.relations);
    }

    @Override
    public String toString() {
        return "Attribute [owner=" + owner.name + ", name=" + name + ", type=" + type + ", relations=" + relations
                + "]";
    }
}
