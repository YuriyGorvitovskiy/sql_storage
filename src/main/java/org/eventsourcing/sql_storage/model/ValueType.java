package org.eventsourcing.sql_storage.model;

import static org.eventsourcing.sql_storage.model.Container.LIST;
import static org.eventsourcing.sql_storage.model.Container.MAP;
import static org.eventsourcing.sql_storage.model.Container.SINGLE;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import org.eventsourcing.sql_storage.util.Helper;

public class ValueType {
    public final static ValueType                                 BOOLEAN         = new ValueType(Primitive.BOOLEAN, SINGLE);
    public final static ValueType                                 INTEGER         = new ValueType(Primitive.INTEGER, SINGLE);
    public final static ValueType                                 FLOATING        = new ValueType(Primitive.FLOATING, SINGLE);
    public final static ValueType                                 DATETIME        = new ValueType(Primitive.DATETIME, SINGLE);
    public final static ValueType                                 IDENTIFIER      = new ValueType(Primitive.IDENTIFIER, SINGLE);
    public final static ValueType                                 STRING          = new ValueType(Primitive.STRING, SINGLE);
    public final static ValueType                                 TEXT            = new ValueType(Primitive.TEXT, SINGLE);
    public final static ValueType                                 REFERENCE       = new ValueType(Primitive.REFERENCE, SINGLE);

    public final static ValueType                                 BOOLEAN_LIST    = new ValueType(Primitive.BOOLEAN, LIST);
    public final static ValueType                                 INTEGER_LIST    = new ValueType(Primitive.INTEGER, LIST);
    public final static ValueType                                 FLOATING_LIST   = new ValueType(Primitive.FLOATING, LIST);
    public final static ValueType                                 DATETIME_LIST   = new ValueType(Primitive.DATETIME, LIST);
    public final static ValueType                                 IDENTIFIER_LIST = new ValueType(Primitive.IDENTIFIER, LIST);
    public final static ValueType                                 STRING_LIST     = new ValueType(Primitive.STRING, LIST);
    public final static ValueType                                 TEXT_LIST       = new ValueType(Primitive.TEXT, LIST);
    public final static ValueType                                 REFERENCE_LIST  = new ValueType(Primitive.REFERENCE, LIST);

    public final static ValueType                                 BOOLEAN_MAP     = new ValueType(Primitive.BOOLEAN, MAP);
    public final static ValueType                                 INTEGER_MAP     = new ValueType(Primitive.INTEGER, MAP);
    public final static ValueType                                 FLOATING_MAP    = new ValueType(Primitive.FLOATING, MAP);
    public final static ValueType                                 DATETIME_MAP    = new ValueType(Primitive.DATETIME, MAP);
    public final static ValueType                                 IDENTIFIER_MAP  = new ValueType(Primitive.IDENTIFIER, MAP);
    public final static ValueType                                 STRING_MAP      = new ValueType(Primitive.STRING, MAP);
    public final static ValueType                                 TEXT_MAP        = new ValueType(Primitive.TEXT, MAP);
    public final static ValueType                                 REFERENCE_MAP   = new ValueType(Primitive.REFERENCE, MAP);

    public final static Map<Primitive, ValueType>                 ALL_SINGLE;
    public final static Map<Primitive, ValueType>                 ALL_LIST;
    public final static Map<Primitive, ValueType>                 ALL_MAP;
    public final static Map<Container, Map<Primitive, ValueType>> ALL;

    static {
        Map<Primitive, ValueType> singles = new EnumMap<>(Primitive.class);
        Map<Primitive, ValueType> lists   = new EnumMap<>(Primitive.class);
        Map<Primitive, ValueType> maps    = new EnumMap<>(Primitive.class);

        for (ValueType type : Helper.valuesOfPublicStaticFinalFields(ValueType.class, ValueType.class)) {
            switch (type.container) {
                case LIST:
                    lists.put(type.primitive, type);
                    break;
                case MAP:
                    maps.put(type.primitive, type);
                    break;
                case SINGLE:
                    singles.put(type.primitive, type);
                    break;
            }
        }
        Map<Container, Map<Primitive, ValueType>> all = new EnumMap<>(Container.class);
        all.put(SINGLE, singles);
        all.put(LIST, lists);
        all.put(MAP, maps);

        ALL_SINGLE = Collections.unmodifiableMap(singles);
        ALL_LIST = Collections.unmodifiableMap(lists);
        ALL_MAP = Collections.unmodifiableMap(maps);
        ALL = Collections.unmodifiableMap(all);
    };

    public final Primitive primitive;
    public final Container container;

    private ValueType(Primitive primitive, Container container) {
        this.primitive = primitive;
        this.container = container;
    }

    public static ValueType typeOf(Primitive primitive) {
        return ALL_SINGLE.get(primitive);
    }

    public static ValueType listOf(Primitive primitive) {
        return ALL_LIST.get(primitive);
    }

    public static ValueType mapOf(Primitive primitive) {
        return ALL_MAP.get(primitive);
    }

    public static ValueType typeOf(Primitive primitive, Container container) {
        return ALL.get(container).get(primitive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(container, primitive);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return primitive + (container == SINGLE ? "" : "-" + container);
    }

}
