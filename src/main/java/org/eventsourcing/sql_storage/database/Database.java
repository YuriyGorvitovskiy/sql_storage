package org.eventsourcing.sql_storage.database;

import java.util.Collections;
import java.util.Set;

import org.eventsourcing.sql_storage.util.Helper;

public class Database {

    final int         maxidentifierLength;
    final Set<String> reservedKeywords;

    protected Database(int maxidentifierLength, String reservedWordsResource) {
        this.maxidentifierLength = maxidentifierLength;
        this.reservedKeywords = Collections.unmodifiableSet(
            Helper.resourceAsDictionary(getClass(), reservedWordsResource));
    }
}
