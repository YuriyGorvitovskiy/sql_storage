package org.eventsourcing.sql_storage.model;

import java.util.Objects;

import org.eventsourcing.sql_storage.util.Utils;

public class ValueType {

    final ContainerType containerType;
    final PrimitiveType primitiveType;

    public ValueType(ContainerType containerType, PrimitiveType primitiveType) {
	this.containerType = containerType;
	this.primitiveType = primitiveType;
    }

    public ContainerType getContainerType() {
	return containerType;
    }

    public PrimitiveType getPrimitiveType() {
	return primitiveType;
    }

    @Override
    public int hashCode() {
	return Objects.hash(containerType, primitiveType);
    }

    @Override
    public boolean equals(Object obj) {
	Boolean check = Utils.equalsCheck(this, obj);
	if (null != check)
	    return check;

	ValueType other = (ValueType) obj;
	return Objects.equals(this.containerType, other.containerType)
		&& Objects.equals(this.primitiveType, other.primitiveType);
    }

    @Override
    public String toString() {
	return "ValueType [containerType=" + containerType + "" + ", primitiveType=" + primitiveType + "]";
    }

}
