package com.education.smsencrypt.database.database_adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.education.smsencrypt.database.TableSendReceive;
import com.education.smsencrypt.utils.DatabaseManager;
import com.education.smsencrypt.utils.listItemObject.ListItemRecieveSMS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eno on 3/27/2016.
 */
public class TableSendRecieveAdapter {
    private Context context;
    private DatabaseManager dbManager;
    private SQLiteDatabase db;

    public TableSendRecieveAdapter(Context context) {
        this.context = context;
    }

    public TableSendRecieveAdapter open() throws SQLException {
        dbManager = new DatabaseManager(context);
        dbManager.pushReferCount();
        db = dbManager.getWritableDatabase();
        return this;
    }

    public void close() {
        dbManager.close();
    }

    private ContentValues createContentValues(String number, String konten, String date, String icon, String status) {
        ContentValues values = new ContentValues();
        values.put(TableSendReceive.Snumber, number);
        values.put(TableSendReceive.Skonten, konten);
        values.put(TableSendReceive.Sdate, date);
        values.put(TableSendReceive.Sicon, icon);
        values.put(TableSendReceive.Sstatus, status);
        return values;
    }

    /**
     * Process insert data to table
     */
    public void insert(String number, String konten, String date, String icon, String status) {
        dbManager.pushReferCount();
        try {
            db.beginTransaction();
            try {
                ContentValues values = createContentValues(number, konten, date, icon, status);
                db.insert(TableSendReceive.TABLE_NAME, null, values);
                db.setTransactionSuccessful();
                //Toast.makeText(context,"Data saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        } finally {
            dbManager.popReferCount();
        }
    }

    /**
     * process delete all of data
     */
    public boolean deleteAll() {
        dbManager.pushReferCount();
        int col = 0;
        boolean ret = false;
        try {
            db.beginTransaction();
            try {
                col = db.delete(TableSendReceive.TABLE_NAME, null, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        } finally {
            dbManager.popReferCount();
        }
        if (col > 0) {
            ret = true;
        }
        return ret;
    }

    /**
     * process fetch of data
     */
    public List<ListItemRecieveSMS> fetchall(Context context) throws SQLException {
        dbManager = new DatabaseManager(context);
        dbManager.pushReferCount();
        try {
            List<ListItemRecieveSMS> ret = new ArrayList<ListItemRecieveSMS>();
            ListItemRecieveSMS item;
            db = dbManager.getReadableDatabase();
            String sql;
            Cursor cursor;
            sql = "SELECT * FROM  " + TableSendReceive.TABLE_NAME;
            cursor = db.rawQuery(sql, null);
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    item = new ListItemRecieveSMS(context);
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Snumber)))
                        item.setFrom(cursor.getString(cursor.getColumnIndex(TableSendReceive.Snumber)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Skonten)))
                        item.setContent(cursor.getString(cursor.getColumnIndex(TableSendReceive.Skonten)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Sdate)))
                        item.setDate(cursor.getString(cursor.getColumnIndex(TableSendReceive.Sdate)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Sicon)))
                        item.setFirtIcon(cursor.getString(cursor.getColumnIndex(TableSendReceive.Sicon)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Sstatus)))
                        item.setStatus(cursor.getString(cursor.getColumnIndex(TableSendReceive.Sstatus)));
                    ret.add(item);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
            return ret;
        } finally {
            dbManager.popReferCount();
        }
    }

    /**
     * process fetch of data
     */
    public List<ListItemRecieveSMS> fetchbycondition(Context context, String condtion, String param) throws SQLException {
        dbManager = new DatabaseManager(context);
        dbManager.pushReferCount();
        try {
            List<ListItemRecieveSMS> ret = new ArrayList<ListItemRecieveSMS>();
            ListItemRecieveSMS item;
            db = dbManager.getReadableDatabase();
            String sql;
            String[] args= {
                    param
            };
            Cursor cursor;
            sql = "SELECT * FROM  " + TableSendReceive.TABLE_NAME + " WHERE " + condtion + " = ? ";
            cursor = db.rawQuery(sql, args);
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    item = new ListItemRecieveSMS(context);
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Snumber)))
                        item.setFrom(cursor.getString(cursor.getColumnIndex(TableSendReceive.Snumber)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Skonten)))
                        item.setContent(cursor.getString(cursor.getColumnIndex(TableSendReceive.Skonten)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Sdate)))
                        item.setDate(cursor.getString(cursor.getColumnIndex(TableSendReceive.Sdate)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Sicon)))
                        item.setFirtIcon(cursor.getString(cursor.getColumnIndex(TableSendReceive.Sicon)));
                    if (!cursor.isNull(cursor.getColumnIndex(TableSendReceive.Sstatus)))
                        item.setStatus(cursor.getString(cursor.getColumnIndex(TableSendReceive.Sstatus)));
                    ret.add(item);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
            return ret;
        } finally {
            dbManager.popReferCount();
        }
    }
}
