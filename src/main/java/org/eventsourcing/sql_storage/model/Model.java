package org.eventsourcing.sql_storage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Model {

    final Map<String, EntityType> entityTypes;

    public Model(Collection<EntityType> entityTypes) {
        Map<String, EntityType> map = new HashMap<>();
        for (EntityType entityType : entityTypes) {
            if (null != entityType.owner)
                throw new RuntimeException(
                    "Entity Type " + entityType + " already attached to the Model " + entityType.owner);

            EntityType duplicate = map.put(entityType.getName(), entityType);
            if (null != duplicate)
                throw new RuntimeException("Model has duplicate Entity Type names: " + duplicate + " & " + entityType);

            entityType.owner = this;
        }
        this.entityTypes = Collections.unmodifiableMap(map);
    }

    public EntityType getEntityType(String entityTypeName) {
        return entityTypes.get(entityTypeName);
    }

    public Map<String, EntityType> getEntityTypes() {
        return entityTypes;
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
