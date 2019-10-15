package org.eventsourcing.sql_storage.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import org.eventsourcing.sql_storage.test.Asserts;

public class Ref_UnitTest {

    @Test
    public void constructor_from_toString() {
        // Setup
        long TYPE_ID = 0x1234567890ABCDEFL;
        long ITEM_ID = 0xFEDCBA0987654321L;

        // Execute
        Ref r1 = new Ref(TYPE_ID, ITEM_ID);
        Ref r2 = Ref.from(r1.toString());
        Ref r3 = Ref.from("");
        Ref r4 = Ref.from(null);

        // Verify
        assertEquals(TYPE_ID, r1.typeId);
        assertEquals(ITEM_ID, r1.itemId);

        assertEquals(TYPE_ID, r2.typeId);
        assertEquals(ITEM_ID, r2.itemId);

        assertNull(r3);
        assertNull(r4);

        assertEquals("1234567890ABCDEF:FEDCBA0987654321", r2.toString());
    }

    @Test
    public void from_exception() {
        // Execute
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Ref.from(":123"));

        // Verify
        assertEquals("Wrong Reference format", thrown.getMessage());
    }

    @Test
    public void equals_hash() {
        // Setup
        long TYPE_ID1  = 0x1234567890ABCDEFL;
        long TYPE_ID2  = 0x90ABCDEFL;
        long ITEM_ID1  = 0xFEDCBA0987654321L;
        long ITEM_ID2  = 0xFEDCBA09L;

        Ref  REF1      = new Ref(TYPE_ID1, ITEM_ID1);
        Ref  REF2      = new Ref(TYPE_ID1, ITEM_ID2);
        Ref  REF3      = new Ref(TYPE_ID2, ITEM_ID1);
        Ref  REF4      = new Ref(TYPE_ID2, ITEM_ID2);
        Ref  REF1_COPY = new Ref(TYPE_ID1, ITEM_ID1);

        // Execute & Verify
        Asserts.assertEquality(REF1, REF1_COPY);
        Asserts.assertInequality(null, REF1, REF2, REF3, REF4);
    }
}
