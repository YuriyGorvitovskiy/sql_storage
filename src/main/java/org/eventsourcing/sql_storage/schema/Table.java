package org.eventsourcing.sql_storage.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.eventsourcing.sql_storage.util.Helper;

public class Table {

    public static class Builder {
        String                         name;
        List<Consumer<Column.Builder>> columnBuilders = new ArrayList<>();
        List<Consumer<Index.Builder>>  indexBuilders  = new ArrayList<>();

        Builder() {
        }

        public Builder name(String name) {
            this.name = name.trim();
            return this;
        }

        public Builder column(Consumer<Column.Builder> columnDefiner) {
            this.columnBuilders.add(columnDefiner);
            return this;
        }

        public Builder index(Consumer<Index.Builder> indexDefiner) {
            this.indexBuilders.add(indexDefiner);
            return this;
        }

        public Builder column(String name, DataType type) {
            return column((c) -> c
                .name(name)
                .type(type));
        }

        public Builder index(String name, Consumer<Index.Builder> indexBuilders) {
            return index((i) -> indexBuilders.accept(i.name(name)));
        }

        public Builder index(String name, String... columnNames) {
            return index(name, (i) -> {
                for (String columnName : columnNames) {
                    i.column(columnName);
                }
            });
        }

        public Builder primaryKey(String name, String... columnNames) {
            return index(name, (i) -> {
                i.primary();
                for (String columnName : columnNames) {
                    i.column(columnName);
                }
            });
        }

        public Table build() {
            if (Helper.isEmpty(name))
                throw new RuntimeException("Table has no name specified");

            if (columnBuilders.isEmpty())
                throw new RuntimeException("Table " + name + " has no columns specified");

            Map<String, Column> columnMap = new HashMap<>();
            Map<String, Index> indexMap = new HashMap<>();
            Table table = new Table(name, columnMap, indexMap);

            for (Consumer<Column.Builder> columnDefiner : columnBuilders) {
                Column.Builder builder = new Column.Builder();
                columnDefiner.accept(builder);

                Column column = builder.build(table);
                Column duplicate = columnMap.put(column.name, column);
                if (null != duplicate)
                    throw new RuntimeException(
                        "Table has duplicate column names: " + duplicate + " & " + column);
            }

            for (Consumer<Index.Builder> indexDefiner : indexBuilders) {
                Index.Builder builder = new Index.Builder();
                indexDefiner.accept(builder);

                Index index = builder.build(table);
                Index duplicate = indexMap.put(index.name, index);
                if (null != duplicate)
                    throw new RuntimeException(
                        "Table has duplicate index names: " + duplicate + " & " + index);
            }

            return table;
        }

    }

    public final String              name;
    public final Map<String, Column> columns;
    public final Map<String, Index>  indexes;

    Table(String name, Map<String, Column> columns, Map<String, Index> indexes) {
        this.name = name;
        this.columns = Collections.unmodifiableMap(columns);
        this.indexes = Collections.unmodifiableMap(indexes);
    }

    public Column getColumn(String name) {
        return columns.get(name);
    }

    public Index getIndex(String name) {
        return indexes.get(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns, indexes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Table))
            return false;

        Table other = (Table) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.columns, other.columns)
                && Objects.equals(this.indexes, other.indexes);
    }

    @Override
    public String toString() {
        return "Table [name=" + name + "]";
    }
}
