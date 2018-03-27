package org.eventsourcing.sql_storage.database;

public class Postgres extends Database {
    public static final Postgres DATABASE = new Postgres();

    private Postgres() {
        super(63, 63, "PostgresKeywords.txt");
    }

}
