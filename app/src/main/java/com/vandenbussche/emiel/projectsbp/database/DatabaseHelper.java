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


    //zie ook onderaan voor versie bestemd tijdens de ontwikkeling van app
    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        while (oldVersion < newVersion) {
            switch (oldVersion) {
                case 0:
                    upgradeTo1(db);
                    oldVersion++;
                    break;
                case 1:
                    //upgrade logic from version 1 to 2
                case 2:
                    //upgrade logic from version 2 to 3
                case 3:
                    //upgrade logic from version 3 to 4
                    break;
                default:
                    throw new IllegalStateException(
                            "onUpgrade() with unknown oldVersion " + oldVersion);
            }
        }
    }

    private void upgradeTo1(SQLiteDatabase db) {
        db.execSQL(Contract.PollsDB.CREATE_TABLE);
    }


/*    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.PollsDB.CREATE_TABLE);
    }

    //For development time schema upgrades where data loss is not an issue
    //remove your existing tables and call onCreate() to recreate the database.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(Contract.PollsDB.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }*/

}
