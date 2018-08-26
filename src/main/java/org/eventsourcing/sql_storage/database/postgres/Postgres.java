package org.eventsourcing.sql_storage.database.postgres;

import org.eventsourcing.sql_storage.database.Database;

public class Postgres extends Database {

    public Postgres() {
        super(new PostgresSQLGenerator(), 63, "PostgresKeywords.txt");
    }

}
