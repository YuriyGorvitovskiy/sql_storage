package org.eventsourcing.sql_storage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AttributeModel {

    final String name;
    final ValueType type;
    final Map<String, RelationTarget> targets;

    // Can be changed only by EntityModel Constructor
    EntityModel owner;

    public AttributeModel(String name, ValueType type) {
	this.name = name;
	this.type = type;
	this.targets = null;
    }

    public AttributeModel(String name, ContainerType type, Collection<RelationTarget> targets) {
	this.name = name;
	this.type = new ValueType(type, PrimitiveType.REFERENCE);

	Map<String, RelationTarget> map = new HashMap<>();
	for (RelationTarget target : targets) {
	    RelationTarget duplicate = map.put(target.getEntityName(), target);
	    if (null != duplicate)
		throw new RuntimeException(
			"Attribute " + name + " has duplicate target entities: " + duplicate + " & " + target);
	}
	this.targets = Collections.unmodifiableMap(map);
    }

    public String getName() {
	return name;
    }

    public ValueType getType() {
	return type;
    }

    public Object getOwner() {
	return owner;
    }

    public Map<String, RelationTarget> getTargets() {
	return targets;
    }

    public RelationTarget getTarget(String entityName) {
	return null != targets ? targets.get(entityName) : null;
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
