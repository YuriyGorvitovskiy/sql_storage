package org.eventsourcing.sql_storage.mapping;

import org.eventsourcing.sql_storage.data.Ref;

public interface MappingAttribute {

    public MappingValue get(Ref ref);
}
