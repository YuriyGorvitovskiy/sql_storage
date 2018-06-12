package org.eventsourcing.sql_storage.mapping;

import org.eventsourcing.sql_storage.schema.Column;

public class MappingValue {
    public final Column  columnId;
    public final Column  columnKey;
    public final Column  columnValue;
    public final boolean removable;

    public MappingValue(Column columnId, Column columnKey, Column columnValue, boolean removable) {
        this.columnId = columnId;
        this.columnKey = columnKey;
        this.columnValue = columnValue;
        this.removable = removable;
    }
}
