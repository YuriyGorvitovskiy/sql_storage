package org.eventsourcing.sql_storage.database.postgres;

import org.eventsourcing.sql_storage.database.SQLGenerator;
import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Index;
import org.eventsourcing.sql_storage.schema.Table;

public class PostgresSQLGenerator extends SQLGenerator {

    @Override
    public String createTable(Table table) {
        StringBuilder tableSQL = new StringBuilder();
        tableSQL.append("CREATE TABLE ");
        tableSQL.append(table.name);
        tableSQL.append(" (\n");
        String tableSQLSeparator = "    ";
        for (Column column : table.columns.values()) {
            tableSQL.append(tableSQLSeparator);
            tableSQL.append(column.name);
            tableSQL.append("  ");
            tableSQL.append(toSQLType(column.type));
            tableSQLSeparator = ",\n    ";
        }

        StringBuilder indexSQL = new StringBuilder();
        for (Index index : table.indexes.values()) {
            if (index.primary) {
                tableSQL.append(tableSQLSeparator);
                tableSQL.append("    PRIMARY KEY ");
                indexSQL.append(columnNames(index.columns));
                tableSQLSeparator = ",\n    ";
            } else {
                indexSQL.append("CREATE INDEX ");
                indexSQL.append(index.name);
                indexSQL.append(" ON ");
                indexSQL.append(table.name);
                indexSQL.append(" ");
                indexSQL.append(columnNames(index.columns));
                indexSQL.append(";\n");
            }
        }
        tableSQL.append(");\n");
        tableSQL.append(indexSQL);
        return tableSQL.toString();
    }

    @Override
    public String toSQLType(DataType type) {
        switch (type) {
            case BOOLEAN:
                return "boolean";
            case DATETIME:
                return "timestamp with time zone";
            case FLOATING:
                return "double precision";
            case IDENTIFIER:
                return "character varying (256)";
            case INTEGER:
                return "bigint";
            case TEXT:
                return "text";
            case STRING:
                return "character varying (1024)";
            default:
                throw new RuntimeException("Unknown datatype: " + type);
        }
    }

}
