package org.eventsourcing.sql_storage.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class MappingModel_UnitTest {

    @Test
    public void constructor_getters() {
        // Setup
        final String               TYPE_1   = "type_1";
        final String               TYPE_2   = "type_2";

        final MappingEntity        mapping1 = new MappingEntity(Collections.emptyMap());
        final MappingEntity        mapping2 = new MappingEntity(Collections.emptyMap());

        Map<String, MappingEntity> mapping  = new HashMap<>();
        mapping.put(TYPE_1, mapping1);
        mapping.put(TYPE_2, mapping2);

        // Execute
        MappingModel subject = new MappingModel(mapping);

        // Verify
        assertEquals(mapping, subject.entities);
    }
}
