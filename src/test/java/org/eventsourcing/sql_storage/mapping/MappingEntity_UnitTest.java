package org.eventsourcing.sql_storage.mapping;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MappingEntity_UnitTest {

    @Test
    public void constructor_getters() {
        // Setup
        final String ATTR_1 = "attr_1";
        final String ATTR_2 = "attr_2";

        final MappingScalar mapping1 = new MappingScalar(null);
        final MappingScalar mapping2 = new MappingScalar(null);

        Map<String, MappingAttribute> mapping = new HashMap<>();
        mapping.put(ATTR_1, mapping1);
        mapping.put(ATTR_2, mapping2);

        // Execute
        MappingEntity subject = new MappingEntity(mapping);

        // Verify
        assertEquals(mapping, subject.attributes);
    }
}
