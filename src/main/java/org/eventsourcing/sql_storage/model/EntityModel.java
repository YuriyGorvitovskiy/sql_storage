package org.eventsourcing.sql_storage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EntityModel {

    final String name;

    final Map<String, AttributeModel> attributes;

    public EntityModel(String name, Collection<AttributeModel> attributes) {
	this.name = name;

	Map<String, AttributeModel> map = new HashMap<>();
	for (AttributeModel attribute : attributes) {
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

    public Map<String, AttributeModel> getAttributes() {
	return attributes;
    }

    public AttributeModel getAttribute(String name) {
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

	if (!(obj instanceof EntityModel))
	    return false;

	EntityModel other = (EntityModel) obj;
	return Objects.equals(this.name, other.name) && Objects.equals(this.attributes, other.attributes);
    }

    @Override
    public String toString() {
	return "EntityModel [name=" + name + ", attributes=" + attributes.values() + "]";
    }
}
