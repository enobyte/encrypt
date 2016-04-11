package com.education.smsencrypt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.education.smsencrypt.database.TableSendReceive;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Enobyte on 08/09/2015.
 */
public class DatabaseManager {
    private final Handler mSubThreadHandler;
    private final ReentrantLock mReferenceCountLock = new ReentrantLock();
    private volatile int mReferenceCount = 0;
    private volatile String mCurrentReadTarget;
    private final ReentrantReadWriteLock mCurrentReadTargetReadWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock mCurrentReadTargetReadLock = mCurrentReadTargetReadWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock mCurrentReadTargetWriteLock = mCurrentReadTargetReadWriteLock.writeLock();
    private boolean isForceSyncShelfDB = false;
    private volatile DatabaseHelper dbHelper;
    private Context _context;
    String deviceID = "";

    public DatabaseManager(Context context) {
        _context = context;
        HandlerThread ht = new HandlerThread("DbMgrHandler",
                android.os.Process.THREAD_PRIORITY_FOREGROUND);
        ht.setPriority(Thread.MAX_PRIORITY);
        ht.start();
        mSubThreadHandler = new Handler(ht.getLooper());
    }


    public SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    private void pushReferenceCount() {
        mReferenceCountLock.lock();
        try {
            mSubThreadHandler.removeCallbacks(mDbHelperCloseRunnable);
            Log.d("DBManager", "MGDatabaseManager#pushReferenceCount() mReferenceCount=" + mReferenceCount + "call from" + Thread.currentThread().getStackTrace()[3]);
            if (mReferenceCount == 0 && dbHelper == null) {
                Log.d("DBManager", "MGDatabaseManager#pushReferenceCount() create dbHelper");
                dbHelper = new DatabaseHelper(_context);
            }
            mReferenceCount++;
        } finally {
            mReferenceCountLock.unlock();
        }
    }

    private void popReferenceCount() {
        mReferenceCountLock.lock();
        try {
            Log.d("DBManager", "MGDatabaseManager#popReferenceCount() mReferenceCount=" + mReferenceCount + "call from" + Thread.currentThread().getStackTrace()[3]);
            mReferenceCount--;
            if (mReferenceCount == 0) {
                requestCloseDbHelper();
            }
        } finally {
            mReferenceCountLock.unlock();
        }
    }

    private void requestCloseDbHelper() {
        mSubThreadHandler.removeCallbacks(mDbHelperCloseRunnable);
        mSubThreadHandler.postDelayed(mDbHelperCloseRunnable, Config.DB_CLOSE_DELAY_TIME);
    }

    private void closeDbHelper() {
        mReferenceCountLock.lock();
        try {
            if (dbHelper == null || mReferenceCount != 0) {
                return;
            }
            dbHelper.close();
            dbHelper = null;
            Log.d("DBManager", "MGDatabaseManager#closeDbHelper() close dbHelper");
        } finally {
            mReferenceCountLock.unlock();
        }
    }

    public void close() {
        closeDbHelper();
    }

    public void pushReferCount() {
        pushReferenceCount();
    }

    public void popReferCount() {
        popReferenceCount();
    }

    interface WriteTransactinableRunner {
        int run(SQLiteDatabase db);
    }

    public int doWriteTransactionEvent(WriteTransactinableRunner run) {
        int ret = 0;
        pushReferenceCount();
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ret = run.run(db);
            // db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            popReferenceCount();
        }
        return ret;
    }

    private final Runnable mDbHelperCloseRunnable = new Runnable() {
        @Override
        public void run() {
            closeDbHelper();
        }
    };

    /**
     * Database Helper
     */
    class DatabaseHelper extends SQLiteOpenHelper {
        private final Context mContext;
        public DatabaseHelper(Context context) {
            super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                //TableAndroidAppVersion.onCreate(db);
                TableSendReceive.onCreate(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (Config.DATABASE_VERSION <= oldVersion) {
                return;
            }
            db.beginTransaction();
            try {
                //TableAndroidAppVersion.onUpgrade(db, oldVersion, newVersion);
                TableSendReceive.onUpgrade(db, oldVersion, newVersion);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Set mobile IMEI
    |-----------------------------------------------------------------------------------------------
    */
    public void setIMEI() {
        final TelephonyManager tm =(TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();
        SharedPreferences imeiPref = _context.getSharedPreferences(Config.KEY_IMEI, Context.MODE_PRIVATE);
        SharedPreferences.Editor shareEditor = imeiPref.edit();
        shareEditor.putString("device_id", deviceID);
        shareEditor.commit();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Get mobile IMEI
    |-----------------------------------------------------------------------------------------------
    */
    public String getIMEI() {
        SharedPreferences sessionpref = _context.getSharedPreferences(Config.KEY_IMEI, Context.MODE_PRIVATE);
        deviceID = sessionpref.getString("device_id", null);
        Log.d("DatabaseManager", "imei= " + deviceID);
        return deviceID;
    }
}
