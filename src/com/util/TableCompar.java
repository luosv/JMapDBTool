package com.util;

import com.dblog.ColumnInfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 表结构对比
 *
 * @author 赵聪慧
 * @2012-11-1 下午9:00:10
 */
public class TableCompar {

    private HashMap<String, List<String>> changeMap = new HashMap<>();
    private static TableCompar instance = new TableCompar();

    public static TableCompar getInstance() {
        return instance;
    }

    private TableCompar() {
        initChangeMap();
    }

    private void initChangeMap() {
//		changeMap.put(key, value);
        List<String> bigintlist = new ArrayList<>();
        bigintlist.add("varchar");
        bigintlist.add("longtext");
        bigintlist.add("text");
        bigintlist.add("bigint");
        bigintlist.add("integer");
        bigintlist.add("int");
        bigintlist.add("int unsigend");
        bigintlist.add("bit");
        bigintlist.add("float");
        bigintlist.add("double");
        changeMap.put("bigint", bigintlist);

        List<String> bitlist = new ArrayList<>();
        bitlist.add("longtext");
        bitlist.add("varchar");
        bitlist.add("text");
        bitlist.add("bigint");
        bitlist.add("integer");
        bitlist.add("int");
        bitlist.add("int unsigend");
        bitlist.add("bit");
        changeMap.put("bit", bitlist);

        List<String> intlist = new ArrayList<>();
        intlist.add("longtext");
        intlist.add("varchar");
        intlist.add("text");
        intlist.add("bigint");
        intlist.add("integer");
        intlist.add("int");
        intlist.add("int unsigned");
        intlist.add("float");
        intlist.add("double");
        changeMap.put("int", intlist);

        changeMap.put("integer", intlist);

        List<String> smallint = new ArrayList<>();
        smallint.add("longtext");
        smallint.add("varchar");
        smallint.add("text");
        smallint.add("bigint");
        smallint.add("integer");
        smallint.add("int");
        smallint.add("int unsigned");
        smallint.add("float");
        smallint.add("double");
        smallint.add("short");
        changeMap.put("smallint", smallint);

        List<String> shortlist = new ArrayList<>();
        shortlist.add("longtext");
        shortlist.add("varchar");
        shortlist.add("text");
        shortlist.add("bigint");
        shortlist.add("int");
        shortlist.add("integer");
        shortlist.add("short");
        shortlist.add("smallint");
        changeMap.put("short", shortlist);

        List<String> bytelist = new ArrayList<>();
        bytelist.add("longtext");
        bytelist.add("varchar");
        bytelist.add("text");
        bytelist.add("bigint");
        bytelist.add("int");
        bytelist.add("short");
        bytelist.add("integer");
        bytelist.add("smallint");
        changeMap.put("byte", bytelist);

        List<String> varcharlist = new ArrayList<>();
        varcharlist.add("longtext");
        varcharlist.add("varchar");
        varcharlist.add("text");
        varcharlist.add("blob");
        changeMap.put("varchar", varcharlist);

        List<String> text = new ArrayList<>();
        text.add("longtext");
        text.add("text");
        text.add("blob");
        changeMap.put("text", text);

        List<String> longtextlist = new ArrayList<>();
        longtextlist.add("longtext");
        longtextlist.add("blob");
        changeMap.put("longtext", longtextlist);
        //TODO 待补全

        List<String> tinyintlist = new ArrayList<>();
        tinyintlist.add("longtext");
        tinyintlist.add("varchar");
        tinyintlist.add("text");
        tinyintlist.add("bigint");
        tinyintlist.add("int");
        tinyintlist.add("short");
        tinyintlist.add("integer");
        tinyintlist.add("tinyint");
        tinyintlist.add("smallint");
        changeMap.put("tinyint", tinyintlist);

        List<String> floatlist = new ArrayList<>();
        floatlist.add("float");
        floatlist.add("double");
        floatlist.add("varchar");
        floatlist.add("text");
        changeMap.put("float", floatlist);

        List<String> bloblist = new ArrayList<>();
        bloblist.add("blob");
        bloblist.add("text");
        bloblist.add("varchar");
        bloblist.add("longtext");
        changeMap.put("blob", bloblist);
    }

    public String compartor(Connection conn1, Connection conn2, String tableName1, String tableName2) {
        try {
//			String[] tableNames = DBUtils.getTableName(conn1);
//			List<ColumnInfo> clomnDefine =DBUtils.getClomnDefine(conn1, tableNames[0]);
//			for (ColumnInfo columnInfo : clomnDefine) {
//				System.out.println(columnInfo.toDDL());
//			}
        } catch (Exception e) {
        }
        return "";
    }

    public List<String> compartor(String tableName, List<ColumnInfo> source, List<ColumnInfo> target) throws Exception {
        HashMap<String, ColumnInfo> targetmap = new HashMap<>();
        List<String> result = new ArrayList<>();
        for (ColumnInfo columnInfo : target) {
            targetmap.put(columnInfo.getName(), columnInfo);
        }
        for (ColumnInfo sourceinfo : source) {
            ColumnInfo columnInfo = targetmap.get(sourceinfo.getName().toLowerCase());
            if (columnInfo == null) {
                result.add("ALTER TABLE " + tableName + " ADD COLUMN " + sourceinfo.toDDL() + ";");
            } else {
                if (ableChange(sourceinfo, columnInfo)) {
                    String com = compartor(sourceinfo, columnInfo);
                    if (!com.equals("")) {
                        result.add("ALTER TABLE " + tableName + " MODIFY COLUMN " + com + ";");
                    } else {
                    }
                } else {
                    throw new Exception(tableName + " " + sourceinfo.toString() + " to " + columnInfo + "列类型不匹配  无法自动变更");
                }
            }
        }
        return result;
    }

    public String compartor(ColumnInfo info, ColumnInfo info2) {
        if (info.getType().equals("int") || info.getType().equals("integer") || info.getType().startsWith("int")) {
            if (info2.getType().equals("integer") || info2.getType().equals("int") || info2.getType().startsWith("int")) {
                return "";
            }
        }
        if (info.getType().equals("bigint") && info2.getType().equals(info.getType())) {
            return "";
        }
        if (info.getType().equals("text") && info2.getType().equals(info.getType())) {
            return "";
        }
        if (info.getType().equals("longtext") && info2.getType().equals(info.getType())) {
            return "";
        }
        if (info.getType().equals("bit") && info2.getType().equals(info.getType())) {
            return "";
        }

        if (info.getType().equals("tinyint") && info2.getType().equals(info.getType())) {
            return "";
        }

        if (info.getType().equals("smallint") && info2.getType().equals(info.getType())) {
            return "";
        }

        if (info.getType().equals(info2.getType()) && info.getSize() <= info2.getSize() && info.getNullable() == info2.getNullable()) {
            //相同
            return "";
        } else {
            return info.toDDL();
        }
    }

    private boolean ableChange(ColumnInfo info, ColumnInfo info2) {
        List<String> list = changeMap.get(info.getType());
        if (list == null) {
            return false;
        }
        return list.contains(info2.getType());
    }

}
