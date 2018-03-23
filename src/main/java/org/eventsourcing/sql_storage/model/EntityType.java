package org.eventsourcing.sql_storage.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.eventsourcing.sql_storage.util.Helper;

public class EntityType {

    public static class Builder {
        Long                              typeId;
        String                            name;
        List<Consumer<Attribute.Builder>> attributeDefiners = new ArrayList<>();

        Builder() {
        }

        public Builder typeId(long typeId) {
            this.typeId = typeId;
            return this;
        }

        public Builder name(String name) {
            this.name = name.trim();
            return this;
        }

        public Builder attribute(Consumer<Attribute.Builder> attributeDefiner) {
            this.attributeDefiners.add(attributeDefiner);
            return this;
        }

        public Builder attribute(String name, Consumer<Attribute.Builder> attributeDefiner) {
            return attribute((a) -> attributeDefiner.accept(a.name(name)));
        }

        public Builder attribute(String name, ValueType type, Consumer<Attribute.Builder> attributeDefiner) {
            return attribute((a) -> attributeDefiner.accept(a
                .name(name)
                .type(type)));
        }

        public Builder attribute(String name, ValueType type) {
            return attribute((a) -> a
                .name(name)
                .type(type));
        }

        public Builder attribute(String name, ValueType type, String target, String reverse) {
            return attribute((a) -> a
                .name(name)
                .type(type)
                .relation(target, reverse));
        }

        public EntityType build(Model model, List<Consumer<Model>> resolvers) {
            if (null == typeId)
                throw new RuntimeException("Entity Type has no id specified");

            if (Helper.isEmpty(name))
                throw new RuntimeException("Entity Type has no name specified");

            Map<String, Attribute> attributeMap = new HashMap<>();
            EntityType type = new EntityType(model, typeId, name, attributeMap);

            for (Consumer<Attribute.Builder> attributeDefiner : attributeDefiners) {
                Attribute.Builder builder = new Attribute.Builder();
                attributeDefiner.accept(builder);

                Attribute attribute = builder.build(type, resolvers);
                Attribute duplicate = attributeMap.put(attribute.name, attribute);
                if (null != duplicate)
                    throw new RuntimeException(
                        "Model has duplicate Attribute names: " + duplicate + " & " + attribute);
            }
            return type;
        }
    }

    public final Model                  owner;
    public final long                   typeId;
    public final String                 name;
    public final Map<String, Attribute> attributes;

    EntityType(Model owner, long typeId, String name, Map<String, Attribute> attributeMap) {
        this.owner = owner;
        this.typeId = typeId;
        this.name = name;
        this.attributes = Collections.unmodifiableMap(attributeMap);
    }

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, name, attributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof EntityType))
            return false;

        EntityType other = (EntityType) obj;
        return Objects.equals(this.typeId, other.typeId)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.attributes, other.attributes);
    }

    @Override
    public String toString() {
        return "EntityType [name=" + name + "]";
    }

}
