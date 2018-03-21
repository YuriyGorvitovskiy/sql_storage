package org.eventsourcing.sql_storage.model;

import java.util.Objects;

public class Relation {

    public static class Builder {
        String targetName;
        String reverseName;

        Builder() {
        }

        public Builder target(String entityTypeName) {
            this.targetName = entityTypeName.trim();
            return this;
        }

        public Builder reverse(String attributeName) {
            this.reverseName = attributeName.trim();
            return this;
        }

        public Relation build(Model model) {
            EntityType target = model.getEntityType(targetName);
            if (null == target)
                throw new RuntimeException(
                    "Relation target entity type " + targetName + " doesn't exists.");

            Attribute reverse = target.getAttribute(reverseName);
            if (null == reverse)
                throw new RuntimeException(
                    "Reverse relation attribute " + reverseName + " on target type "
                            + targetName + " doesn't exists.");

            if (reverse.type.primitive != Primitive.REFERENCE)
                throw new RuntimeException(
                    "Reverse relation attribute " + reverseName + " on target type "
                            + targetName + " is of type " + reverse.type
                            + ", but should be " + Primitive.REFERENCE + ".");

            return new Relation(target, reverse);
        }
    }

    public final EntityType target;
    public final Attribute  reverse;

    Relation(EntityType target, Attribute reverse) {
        this.target = target;
        this.reverse = reverse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(target.name, reverse.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Relation))
            return false;

        Relation other = (Relation) obj;
        return Objects.equals(this.target.name, other.target.name)
                && Objects.equals(this.reverse.name, other.reverse.name);
    }

    @Override
    public String toString() {
        return "Relation [target=" + target.name + ", reverse=" + reverse.name + "]";
    }

}
