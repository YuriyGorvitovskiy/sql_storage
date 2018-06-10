package org.eventsourcing.sql_storage.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eventsourcing.sql_storage.util.Helper;

public class Index {

    public static class Builder {
        String      name;
        Set<String> columnNames = new LinkedHashSet<>();

        Builder() {
        }

        public Builder name(String name) {
            this.name = name.trim();
            return this;
        }

        public Builder column(String columnName) {
            columnName = columnName.trim();
            if (!this.columnNames.add(columnName)) {
                throw new RuntimeException("Index has duplicate column " + columnName + " specified.");
            }
            return this;
        }

        public Index build(Table owner) {
            if (Helper.isEmpty(name))
                throw new RuntimeException("Index for table " + owner.name + " has no name specified.");

            if (columnNames.isEmpty())
                throw new RuntimeException("Index " + name + " for table " + owner.name + " has no columns specified.");

            List<Column> columnsList = new ArrayList<>();
            for (String columnName : columnNames) {
                Column column = owner.columns.get(columnName);
                if (null == column)
                    throw new RuntimeException("Table " + owner.name + " has no column " + columnName + " specified.");

                columnsList.add(column);
            }
            return new Index(owner, name, columnsList);
        }
    }

    public final Table        table;
    public final String       name;
    public final List<Column> columns;

    Index(Table table, String name, List<Column> columns) {
        this.table = table;
        this.name = name;
        this.columns = Collections.unmodifiableList(columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Index))
            return false;

        Index other = (Index) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.columns, other.columns);
    }

    @Override
    public String toString() {
        return "Index [table=" + table.name + ", name=" + name + "]";
    }
}
