package org.eventsourcing.sql_storage.database;

import java.util.List;

import org.eventsourcing.sql_storage.schema.Column;
import org.eventsourcing.sql_storage.schema.DataType;
import org.eventsourcing.sql_storage.schema.Table;

public abstract class SQLGenerator {

    public abstract String createTable(Table table);

    public String columnNames(List<Column> columns) {
        StringBuilder columnsSQL = new StringBuilder();
        columnsSQL.append("(");
        String columnSQLSeparator = "";
        for (Column column : columns) {
            columnsSQL.append(columnSQLSeparator);
            columnsSQL.append(column.name);
            columnSQLSeparator = ", ";
        }
        columnsSQL.append(")");
        return columnsSQL.toString();
    }

    public abstract String toSQLType(DataType type);
}
