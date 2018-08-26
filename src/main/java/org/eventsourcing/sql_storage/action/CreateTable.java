package org.eventsourcing.sql_storage.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eventsourcing.sql_storage.database.Database;
import org.eventsourcing.sql_storage.schema.Table;

public class CreateTable implements IAction<Table> {

    final Database database;

    CreateTable(Database database) {
        this.database = database;
    }

    @Override
    public void accept(Table table) {
        try (Connection connection = database.datasource.getConnection()) {
            try (PreparedStatement statement = prepare(connection, table)) {
                statement.execute();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private PreparedStatement prepare(Connection connection, Table table) throws SQLException {
        return connection.prepareStatement(database.sqlGenerator.createTable(table));
    }
}
