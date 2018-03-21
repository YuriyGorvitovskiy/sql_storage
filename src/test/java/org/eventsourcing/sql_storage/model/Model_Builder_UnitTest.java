package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.ValueType.INTEGER;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_LIST;
import static org.eventsourcing.sql_storage.model.ValueType.REFERENCE_MAP;
import static org.eventsourcing.sql_storage.model.ValueType.TEXT;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class Model_Builder_UnitTest {

    @Test
    public void test() {

        Model model2 = new Model.Builder()
            .type((t) -> t
                .name("ClassA")
                .attribute((a) -> a
                    .name("attr_a")
                    .type(INTEGER))
                .attribute((a) -> a
                    .name("attr_b")
                    .type(REFERENCE_MAP)
                    .relation((r) -> r
                        .target("ClassB")
                        .reverse("attr_r"))
                    .relation((r) -> r
                        .target("ClassC")
                        .reverse("attr_s"))))
            .type("ClassB", (t) -> t
                .attribute("attr_r", (a) -> a
                    .type(REFERENCE_LIST)
                    .relation("ClassA", "attr_b")))
            .type("ClassC", (t) -> t
                .attribute("attr_s", REFERENCE_LIST, "ClassA", "attr_b")
                .attribute("attr_t", TEXT))
            .build();

        assertNotNull(model2);
    }

}
