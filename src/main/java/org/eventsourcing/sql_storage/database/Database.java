package org.eventsourcing.sql_storage.database;

import java.util.Collections;
import java.util.Set;

import org.eventsourcing.sql_storage.util.Helper;

public class Database {

    final int         maxTableNameLength;
    final int         maxColumnNameLength;
    final Set<String> reservedKeywords;

    protected Database(int maxTableNameLength,
            int maxColumnNameLength,
            String reservedWordsResource) {
        this.maxTableNameLength = maxTableNameLength;
        this.maxColumnNameLength = maxColumnNameLength;
        this.reservedKeywords = Collections.unmodifiableSet(
            Helper.resourceAsDictionary(getClass(), reservedWordsResource));
    }
}
