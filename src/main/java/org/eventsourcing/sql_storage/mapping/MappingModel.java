package org.eventsourcing.sql_storage.mapping;

import java.util.Collections;
import java.util.Map;

public class MappingModel {

    public final Map<String, MappingEntity> entities;

    public MappingModel(Map<String, MappingEntity> entities) {
        this.entities = Collections.unmodifiableMap(entities);
    }

}
