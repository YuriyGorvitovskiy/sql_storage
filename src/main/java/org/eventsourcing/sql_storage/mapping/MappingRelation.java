package org.eventsourcing.sql_storage.mapping;

import java.util.Collections;
import java.util.Map;

import org.eventsourcing.sql_storage.data.Ref;

public class MappingRelation implements MappingAttribute {

    public final Map<Long, MappingValue> relations;

    public MappingRelation(Map<Long, MappingValue> relations) {
        this.relations = Collections.unmodifiableMap(relations);
    }

    @Override
    public MappingValue get(Ref ref) {
        return relations.get(ref.typeId);
    }

}
