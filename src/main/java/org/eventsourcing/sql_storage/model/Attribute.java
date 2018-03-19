package org.eventsourcing.sql_storage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Attribute {

    final String name;
    final ValueType type;
    final Map<String, Relation> targets;

    // Can be changed only by EntityModel Constructor
    EntityType owner;

    public Attribute(String name, ValueType type) {
	this.name = name;
	this.type = type;
	this.targets = null;
    }

    public Attribute(String name, Container type, Collection<Relation> targets) {
	this.name = name;
	this.type = new ValueType(type, Primitive.REFERENCE);

	Map<String, Relation> map = new HashMap<>();
	for (Relation target : targets) {
	    Relation duplicate = map.put(target.getTargetEntityName(), target);
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

    public Map<String, Relation> getTargets() {
	return targets;
    }

    public Relation getTarget(String entityName) {
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

	if (!(obj instanceof Attribute))
	    return false;

	Attribute other = (Attribute) obj;
	return Objects.equals(this.name, other.name) && Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
	return "AttributeModel [name=" + name + ", type=" + type + "]";
    }
}
