package org.eventsourcing.sql_storage.model;

import java.util.Objects;

public class ValueType {

    final Container containerType;
    final Primitive primitiveType;

    public ValueType(Container containerType, Primitive primitiveType) {
	this.containerType = containerType;
	this.primitiveType = primitiveType;
    }

    public Container getContainerType() {
	return containerType;
    }

    public Primitive getPrimitiveType() {
	return primitiveType;
    }

    @Override
    public int hashCode() {
	return Objects.hash(containerType, primitiveType);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;

	if (!(obj instanceof ValueType))
	    return false;

	ValueType other = (ValueType) obj;
	return Objects.equals(this.containerType, other.containerType)
		&& Objects.equals(this.primitiveType, other.primitiveType);
    }

    @Override
    public String toString() {
	return "ValueType [containerType=" + containerType + "" + ", primitiveType=" + primitiveType + "]";
    }

}
