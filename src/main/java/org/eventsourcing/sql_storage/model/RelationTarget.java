package org.eventsourcing.sql_storage.model;

import java.util.Objects;

public class RelationTarget {

    final String entityName;
    final String attributeName;

    public RelationTarget(String entityName, String attributeName) {
	this.entityName = entityName;
	this.attributeName = attributeName;
    }

    public String getEntityName() {
	return entityName;
    }

    public String getAttributeName() {
	return attributeName;
    }

    @Override
    public int hashCode() {
	return Objects.hash(entityName, attributeName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;

	if (!(obj instanceof RelationTarget))
	    return false;

	RelationTarget other = (RelationTarget) obj;
	return Objects.equals(this.entityName, other.entityName)
		&& Objects.equals(this.attributeName, other.attributeName);
    }

    @Override
    public String toString() {
	return "RelationTarget [entityName=" + entityName + ", attributeName=" + attributeName + "]";
    }

}
