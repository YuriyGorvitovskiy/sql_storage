package org.eventsourcing.sql_storage.model;

import java.util.Objects;

public class Relation {

    final String targetEntityName;
    final String reverseAttributeName;

    public Relation(String entityName, String attributeName) {
        this.targetEntityName = entityName;
        this.reverseAttributeName = attributeName;
    }

    public String getTargetEntityName() {
        return targetEntityName;
    }

    public String getReverseAttributeName() {
        return reverseAttributeName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetEntityName, reverseAttributeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Relation))
            return false;

        Relation other = (Relation) obj;
        return Objects.equals(this.targetEntityName, other.targetEntityName)
                && Objects.equals(this.reverseAttributeName, other.reverseAttributeName);
    }

    @Override
    public String toString() {
        return "Relation [target=" + targetEntityName + ", reverse=" + reverseAttributeName + "]";
    }

}
