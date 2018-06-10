package org.eventsourcing.sql_storage.model;

import java.util.Objects;

public class AttributeId implements Comparable<AttributeId> {

    final String className;
    final String attributeName;

    public AttributeId(Attribute attribute) {
        this.className = attribute.owner.name;
        this.attributeName = attribute.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, attributeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof AttributeId))
            return false;

        AttributeId other = (AttributeId) obj;
        return Objects.equals(this.className, other.className)
                && Objects.equals(this.attributeName, other.attributeName);
    }

    @Override
    public int compareTo(AttributeId o) {
        int cmp = className.compareTo(o.className);
        if (0 == cmp)
            cmp = attributeName.compareTo(o.attributeName);

        return cmp;
    }

    @Override
    public String toString() {
        return "ModelAttributeid [class=" + className + ", attribute=" + attributeName + "]";
    }

}
