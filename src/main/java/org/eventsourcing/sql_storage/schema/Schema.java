package org.eventsourcing.sql_storage.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Schema {

    public static class Builder {

        List<Consumer<Table.Builder>> tableDefiners = new ArrayList<>();

        public Builder() {
        }

        public Builder table(Consumer<Table.Builder> tableDefiners) {
            this.tableDefiners.add(tableDefiners);
            return this;
        }

        public Builder table(String name, Consumer<Table.Builder> tableDefiner) {
            return table((t) -> tableDefiner.accept(t.name(name)));
        }

        public Schema build() {
            Map<String, Table> tableMap = new HashMap<>();
            for (Consumer<Table.Builder> tableDefiner : tableDefiners) {
                Table.Builder builder = new Table.Builder();
                tableDefiner.accept(builder);

                Table table = builder.build();
                Table duplicate = tableMap.put(table.name, table);
                if (null != duplicate)
                    throw new RuntimeException(
                        "Model has duplicate table names: " + duplicate + " & " + table);
            }
            return new Schema(tableMap);
        }
    }

    public final Map<String, Table> tables;

    Schema(Map<String, Table> tables) {
        this.tables = Collections.unmodifiableMap(tables);
    }

    public Table getTable(String name) {
        return tables.get(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tables);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Schema))
            return false;

        Schema other = (Schema) obj;
        return Objects.equals(this.tables, other.tables);
    }

    @Override
    public String toString() {
        return "Schema@" + Integer.toHexString(super.hashCode()) + " [entityTypes=" + tables.values() + "]";
    }
}
