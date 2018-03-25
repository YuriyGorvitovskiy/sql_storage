package org.eventsourcing.sql_storage.schema;

import static org.eventsourcing.sql_storage.schema.DataType.BOOLEAN;
import static org.eventsourcing.sql_storage.schema.DataType.DATETIME;
import static org.eventsourcing.sql_storage.schema.DataType.FLOATING;
import static org.eventsourcing.sql_storage.schema.DataType.INTEGER;
import static org.eventsourcing.sql_storage.schema.DataType.TEXT;
import static org.eventsourcing.sql_storage.schema.DataType.VARCHAR;

public class Example {
    public static final String TABLE_NAME_1 = "tbl_one";
    public static final String TABLE_NAME_2 = "tbl_two";
    public static final String TABLE_NAME_3 = "tbl_three";

    public static final String COLUMN_NAME_1 = "col_one";
    public static final String COLUMN_NAME_2 = "col_two";
    public static final String COLUMN_NAME_3 = "col_three";

    public static final String INDEX_NAME_1 = "ix_one";
    public static final String INDEX_NAME_2 = "ix_two";
    public static final String INDEX_NAME_3 = "ix_three";

    public static final Schema SCHEMA_1 = new Schema.Builder()
        .table(TABLE_NAME_1, t -> t
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_3, FLOATING)
            .index(INDEX_NAME_1, COLUMN_NAME_1)
            .index(INDEX_NAME_2, COLUMN_NAME_2, COLUMN_NAME_3))
        .table(TABLE_NAME_2, t -> t
            .column(COLUMN_NAME_1, DATETIME)
            .column(COLUMN_NAME_2, VARCHAR)
            .column(COLUMN_NAME_3, TEXT)
            .index(INDEX_NAME_2, COLUMN_NAME_1)
            .index(INDEX_NAME_3, COLUMN_NAME_2, COLUMN_NAME_3))
        .table(TABLE_NAME_3, t -> t
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_3, FLOATING)
            .index(INDEX_NAME_1, COLUMN_NAME_1)
            .index(INDEX_NAME_2, COLUMN_NAME_2, COLUMN_NAME_3))
        .build();

    public static final Schema SCHEMA_2 = new Schema.Builder()
        .table(TABLE_NAME_1, t -> t
            .column(COLUMN_NAME_1, TEXT)
            .column(COLUMN_NAME_2, VARCHAR)
            .column(COLUMN_NAME_3, DATETIME)
            .index(INDEX_NAME_1, COLUMN_NAME_1)
            .index(INDEX_NAME_2, COLUMN_NAME_2, COLUMN_NAME_3))
        .table(TABLE_NAME_2, t -> t
            .column(COLUMN_NAME_1, FLOATING)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_3, BOOLEAN)
            .index(INDEX_NAME_2, COLUMN_NAME_1)
            .index(INDEX_NAME_3, COLUMN_NAME_2, COLUMN_NAME_3))
        .table(TABLE_NAME_3, t -> t
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_3, FLOATING)
            .index(INDEX_NAME_1, COLUMN_NAME_3)
            .index(INDEX_NAME_2, COLUMN_NAME_1, COLUMN_NAME_2))
        .build();

    public static final Schema SCHEMA_3 = new Schema.Builder()
        .table(TABLE_NAME_1, t -> t
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_3, FLOATING)
            .index(INDEX_NAME_1, COLUMN_NAME_1)
            .index(INDEX_NAME_2, COLUMN_NAME_2, COLUMN_NAME_3))
        .table(TABLE_NAME_2, t -> t
            .column(COLUMN_NAME_1, DATETIME)
            .column(COLUMN_NAME_2, VARCHAR)
            .column(COLUMN_NAME_3, TEXT)
            .index(INDEX_NAME_2, COLUMN_NAME_1)
            .index(INDEX_NAME_3, COLUMN_NAME_2, COLUMN_NAME_3))
        .table(TABLE_NAME_3, t -> t
            .column(COLUMN_NAME_1, BOOLEAN)
            .column(COLUMN_NAME_2, INTEGER)
            .column(COLUMN_NAME_3, FLOATING)
            .index(INDEX_NAME_1, COLUMN_NAME_1)
            .index(INDEX_NAME_2, COLUMN_NAME_2, COLUMN_NAME_3))
        .build();

    public static final Table TABLE_1_1 = SCHEMA_1.getTable(TABLE_NAME_1);
    public static final Table TABLE_1_2 = SCHEMA_1.getTable(TABLE_NAME_2);
    public static final Table TABLE_1_3 = SCHEMA_1.getTable(TABLE_NAME_3);

    public static final Table TABLE_2_1 = SCHEMA_2.getTable(TABLE_NAME_1);
    public static final Table TABLE_2_2 = SCHEMA_2.getTable(TABLE_NAME_2);
    public static final Table TABLE_2_3 = SCHEMA_2.getTable(TABLE_NAME_3);

    public static final Table TABLE_3_1 = SCHEMA_3.getTable(TABLE_NAME_1);
    public static final Table TABLE_3_2 = SCHEMA_3.getTable(TABLE_NAME_2);
    public static final Table TABLE_3_3 = SCHEMA_3.getTable(TABLE_NAME_3);

    public static final Column COLUMN_1_1 = TABLE_1_1.getColumn(COLUMN_NAME_1);
    public static final Column COLUMN_1_2 = TABLE_1_1.getColumn(COLUMN_NAME_2);
    public static final Column COLUMN_1_3 = TABLE_1_1.getColumn(COLUMN_NAME_3);

    public static final Column COLUMN_2_1 = TABLE_1_2.getColumn(COLUMN_NAME_1);
    public static final Column COLUMN_2_2 = TABLE_1_2.getColumn(COLUMN_NAME_2);
    public static final Column COLUMN_2_3 = TABLE_1_2.getColumn(COLUMN_NAME_3);

    public static final Column COLUMN_3_1 = TABLE_1_3.getColumn(COLUMN_NAME_1);
    public static final Column COLUMN_3_2 = TABLE_1_3.getColumn(COLUMN_NAME_2);
    public static final Column COLUMN_3_3 = TABLE_1_3.getColumn(COLUMN_NAME_3);

    public static final Index INDEX_1_1 = TABLE_1_1.getIndex(INDEX_NAME_1);
    public static final Index INDEX_1_2 = TABLE_1_1.getIndex(INDEX_NAME_2);

    public static final Index INDEX_2_2 = TABLE_1_2.getIndex(INDEX_NAME_2);
    public static final Index INDEX_2_3 = TABLE_1_2.getIndex(INDEX_NAME_3);

    public static final Index INDEX_3_1 = TABLE_1_3.getIndex(INDEX_NAME_1);
    public static final Index INDEX_3_2 = TABLE_1_3.getIndex(INDEX_NAME_2);
}
