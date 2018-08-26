package org.eventsourcing.sql_storage.database;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import org.eventsourcing.sql_storage.util.Helper;

public abstract class Database {

    public final int maxIdentifierLength;

    public final Set<String> reservedKeywords;

    public final SQLGenerator sqlGenerator;

    public final BasicDataSource datasource;

    protected Database(SQLGenerator sqlGenerator, int maxidentifierLength, String reservedWordsResource) {
        this.datasource = new BasicDataSource();
        this.sqlGenerator = sqlGenerator;

        this.maxIdentifierLength = maxidentifierLength;
        this.reservedKeywords = Collections.unmodifiableSet(
            Helper.resourceAsDictionary(getClass(), reservedWordsResource));
    }
}
