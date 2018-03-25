package org.eventsourcing.sql_storage.schema;

import java.util.Objects;

import org.eventsourcing.sql_storage.util.Helper;

public class Column {

    public static class Builder {

        String   name;
        DataType type;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name.trim();
            return this;
        }

        public Builder type(DataType type) {
            this.type = type;
            return this;
        }

        public Column build(Table owner) {
            if (Helper.isEmpty(name))
                throw new RuntimeException("Column for table " + owner.name + " has no name specified.");

            if (null == type)
                throw new RuntimeException("Column " + name + " for table " + owner.name + " has no type specified.");

            return new Column(owner, name, type);
        }
    }

    public final Table    owner;
    public final String   name;
    public final DataType type;

    Column(Table owner, String name, DataType type) {
        this.owner = owner;
        this.name = name;
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Column))
            return false;

        Column other = (Column) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
        return "Column [owner=" + owner.name + ", name=" + name + ", type=" + type + "]";
    }

}
