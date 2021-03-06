package com.sxd.oracle.analyse.domain;

import com.sxd.oracle.analyse.HexUtils;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author shixiaodong
 * @version 1.0.0
 * @Description 表定义
 * @createTime 2020年04月21日 14:33:00
 */
public class Table {

    /**
     * 表结构定义
     */
    private TableStruct tableStruct;

    /**
     * 批量insert语句
     */
    private InsertStruct insertStruct;

    private List<IndexStruct> indexStructs;

    /**
     * 行数据
     */
    private List<Row> rows;

    public TableStruct getTableStruct() {
        return tableStruct;
    }

    public void setTableStruct(TableStruct tableStruct) {
        this.tableStruct = tableStruct;
    }

    public InsertStruct getInsertStruct() {
        return insertStruct;
    }

    public void setInsertStruct(InsertStruct insertStruct) {
        this.insertStruct = insertStruct;
    }

    public List<IndexStruct> getIndexStructs() {
        return indexStructs;
    }

    public void setIndexStructs(List<IndexStruct> indexStructs) {
        this.indexStructs = indexStructs;
    }

    public void addIndexStruct(IndexStruct indexStruct) {
        if (indexStructs == null) {
            indexStructs = new ArrayList<IndexStruct>();
        }
        indexStructs.add(indexStruct);
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Object decodeColunm(byte[] value, String type) {
        String decode = "";
        if (value[0] == -2 && value[1] == -1) {
            decode = null;
        } else if ("TIMESTAMP".equals(type)) {
            int century = value[0] - 100;
            int year = value[1] - 100;
            int month = value[2] - 1;
            int day = value[3];
            int hour = value[4] - 1;
            int minute = value[5] - 1;
            int second = value[6] - 1;
            byte[] bytes = Arrays.copyOfRange(value, 7, 11);
            String hexStr = HexUtils.bytesToHexString(bytes);
            int milliSecond = Integer.parseInt(hexStr, 16) / 1000 / 1000;
            decode = "TIMESTAMP";
            Date date = new Date(century * 100 + year - 1900, month, day, hour, minute, second);
            Timestamp timestamp = new Timestamp(date.getTime() + milliSecond);
            return timestamp;
        } else if (type.contains("CHAR")) {
            try {
                decode = new String(value, "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return decode;
    }

    public static void main(String[] args) {
        List<String> l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");

        System.out.println(l.toString().replace("[", "(").replace("]", ")"));

    }
}
