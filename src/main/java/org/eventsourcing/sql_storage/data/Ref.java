package org.eventsourcing.sql_storage.data;

import org.eventsourcing.sql_storage.util.Helper;

public class Ref {
    public final long typeId;
    public final long itemId;

    public Ref(long typeId, long itemId) {
        this.typeId = typeId;
        this.itemId = itemId;
    }

    public static Ref from(String id) {
        if (Helper.isEmpty(id))
            return null;
        try {
            String parts[] = id.trim().split(":");
            long typeId = Long.parseUnsignedLong(parts[0], 16);
            long itemId = Long.parseUnsignedLong(parts[1], 16);
            return new Ref(typeId, itemId);
        } catch (Throwable ex) {
            throw new RuntimeException("Wrong Reference format", ex);
        }
    }

    @Override
    public int hashCode() {
        return Long.hashCode(typeId ^ itemId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Ref))
            return false;

        Ref other = (Ref) obj;
        return this.typeId == other.typeId
                && this.itemId == other.itemId;
    }

    @Override
    public String toString() {
        return (Long.toHexString(typeId) + ":" + Long.toHexString(itemId)).toUpperCase();
    }
}
