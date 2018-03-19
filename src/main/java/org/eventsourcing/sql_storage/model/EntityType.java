package org.eventsourcing.sql_storage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EntityType {

    final String name;

    final Map<String, Attribute> attributes;

    public EntityType(String name, Collection<Attribute> attributes) {
	this.name = name;

	Map<String, Attribute> map = new HashMap<>();
	for (Attribute attribute : attributes) {
	    if (null != attribute.owner)
		throw new RuntimeException(
			"Attribute " + attribute + " already attached to the Entity " + attribute.owner);

	    map.put(attribute.getName(), attribute);
	    attribute.owner = this;
	}
	this.attributes = Collections.unmodifiableMap(map);
    }

    public String getName() {
	return name;
    }

    public Map<String, Attribute> getAttributes() {
	return attributes;
    }

    public Attribute getAttribute(String name) {
	return attributes.get(name);
    }

    @Override
    public int hashCode() {
	return Objects.hash(name, attributes);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;

	if (!(obj instanceof EntityType))
	    return false;

	EntityType other = (EntityType) obj;
	return Objects.equals(this.name, other.name) && Objects.equals(this.attributes, other.attributes);
    }

    @Override
    public String toString() {
	return "EntityType [name=" + name + ", attributes=" + attributes.values() + "]";
    }
}
