package com.vandenbussche.emiel.projectsbp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stijn on 26/09/2016.
 */


public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper INSTANCE;
    private static Object object = new Object();

    public static DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            //via keyword synchronized vermijden we dat meerdere threads Databasehelper-object proberen aan te maken
            synchronized (object) {
                INSTANCE = new DatabaseHelper(context.getApplicationContext());     //opm: hier pas object aanmaken!
            }
        }
        return INSTANCE;
    }

    private DatabaseHelper(Context context) {
        //opm: hier wordt database-versienummer doorgegeven
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop vorige table, create nieuwe
        db.execSQL(Contract.PollsDB.DELETE_TABLE);
        db.execSQL(Contract.PollsDB.CREATE_TABLE);

    }

}
