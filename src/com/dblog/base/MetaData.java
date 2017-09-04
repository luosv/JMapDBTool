package com.dblog.base;

import com.dblog.ColumnInfo;

/**
 * @author Administrator
 */
public class MetaData {

    private String fieldName;
    private String fieldType;
    private String fieldIndex;
    private String mate;

    public MetaData(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.mate = "`" + fieldName + "`\t" + fieldType;
    }

    public MetaData(String fieldName, String fieldIndex, int type) {
        this.mate = "`index_" + fieldIndex + "` (" + fieldName + ")";
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getIndex() {
        return fieldIndex;
    }

    @Override
    public String toString() {
        return mate;
    }

    public ColumnInfo toColumnInfo() {
        ColumnInfo info = new ColumnInfo();
        info.setName(getFieldName());
        if (fieldType.contains("(")) {
            String replace = fieldType.replace(")", "");
            String[] split = replace.split("\\(");
            info.setType(split[0].toLowerCase());
            info.setSize(Integer.valueOf(split[1]));
            info.setNullable(true);
        } else {
            info.setType(getFieldType().toLowerCase());
            info.setSize(0);
            info.setNullable(true);
        }
        return info;
    }

}
