package org.eventsourcing.sql_storage.mapping;

import org.eventsourcing.sql_storage.data.Ref;

public class MappingScalar implements MappingAttribute {

    public final MappingValue value;

    public MappingScalar(MappingValue value) {
        this.value = value;
    }

    @Override
    public MappingValue get(Ref ref) {
        return value;
    }

}
