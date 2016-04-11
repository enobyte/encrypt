package com.education.smsencrypt.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Eno on 3/27/2016.
 */
public class TableSendReceive {
    public static final String TABLE_NAME = "smshistory";
    public static final String Snumber = "number";
    public static final String Skonten = "konten";
    public static final String Sdate = "tanggal";
    public static final String Sicon = "icon";
    public static final String Sstatus = "status"; //status = 1 (send), status = 0 (Recieve)

    public static void onCreate(SQLiteDatabase database) {
        String sql = "create table " + TABLE_NAME
                + "(" + Snumber + " varchar, "
                + Skonten + " varchar, "
                + Sdate + " varchar, "
                + Sicon + " varchar, "
                + Sstatus + " varchar);";

        database.execSQL(sql);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
