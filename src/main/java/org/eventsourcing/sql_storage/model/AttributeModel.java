package org.eventsourcing.sql_storage.model;

import java.util.Objects;

public class AttributeModel {

    final String name;
    final ValueType type;

    public AttributeModel(String name, ValueType type) {
	this.name = name;
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public ValueType getType() {
	return type;
    }

    @Override
    public int hashCode() {
	return Objects.hash(name, type);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;

	if (!(obj instanceof AttributeModel))
	    return false;

	AttributeModel other = (AttributeModel) obj;
	return Objects.equals(this.name, other.name) && Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
	return "AttributeModel [name=" + name + ", type=" + type + "]";
    }
}
