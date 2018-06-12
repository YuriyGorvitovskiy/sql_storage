package org.eventsourcing.sql_storage.mapping;

import java.util.Collections;
import java.util.Map;

public class MappingEntity {

    public final Map<String, MappingAttribute> attributes;

    public MappingEntity(Map<String, MappingAttribute> attributes) {
        this.attributes = Collections.unmodifiableMap(attributes);
    }

}
