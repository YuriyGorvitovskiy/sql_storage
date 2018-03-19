package org.eventsourcing.sql_storage.util;

public class Utils {
    private Utils() {
    }

    public static Boolean equalsCheck(Object a, Object b) {
	if (a == b)
	    return Boolean.TRUE;
	if (a == null || b == null)
	    return Boolean.FALSE;

	if (a.getClass() != b.getClass())
	    return Boolean.FALSE;

	return null;
    }

}
