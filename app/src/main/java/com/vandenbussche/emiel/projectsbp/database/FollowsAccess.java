package com.vandenbussche.emiel.projectsbp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandenbussche.emiel.projectsbp.models.Follow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Stijn on 5/10/2016.
 * Thanks to: http://beust.com/weblog/2015/06/01/easy-sqlite-on-android-with-rxjava/
 */
public class FollowsAccess {

    public static final String TAG = "FollowsAccess";

    public static final String[] allColumns = new String[]{
            Contract.FollowsColumns._ID,
            Contract.FollowsColumns.COLUMN_PAGE_ID,
            Contract.FollowsColumns.COLUMN_FLAG,
    };


     /*
    ** Get all
     */

    public static Observable<List<Follow>> getAll(Context context) {

        return makeObservable(getAllCallable(context))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<Follow>> getAllCallable(final Context context) {
        return new Callable<List<Follow>>() {
            @Override
            public List<Follow> call() throws Exception {



                Cursor mData = context.getContentResolver().query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.FOLLOWS_URI, allColumns, null, null, null);

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToFollowList(mData);
            }
        };
    }



      /*
    ** GetByKey
     */

    public static Observable<List<Follow>> getByKey(Context context, String key, String value) {


        return makeObservable(getByKeyCallable(context, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<Follow>> getByKeyCallable(final Context context, final String key, final String value) {
        return new Callable<List<Follow>>() {
            @Override
            public List<Follow> call() throws Exception {


                Cursor mData = context.getContentResolver().query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.FOLLOWS_URI, allColumns,  key + " = ?", new String[]{value}, null);

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToFollowList(mData);
            }
        };
    }




         /*
    ** Insert
     */

    public static Observable<Long> insert(Context context, ContentValues values) {
        return makeObservable(insertCallable(context, values))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Long> insertCallable(final Context context, final ContentValues values) {
        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {

                context.getContentResolver().insert(com.vandenbussche.emiel.projectsbp.database.provider.Contract.FOLLOWS_URI, values);

                return 0l;
            }
        };
    }


     /*
    ** Update
     */

    //todo: content provider gebruiken
    public static Observable<Long> update(Context context, ContentValues values, String key, String value) {
        return makeObservable(updateCallable(context, values, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Long> updateCallable(final Context context, final ContentValues values,
                                                 final String key, final String value) {
        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long changedRows = -1;

                ContentValues values = new ContentValues();
                try {
                    context.getContentResolver().update(com.vandenbussche.emiel.projectsbp.database.provider.Contract.FOLLOWS_URI, values, key + " = ?", new String[]{value});

                    return 0l;

                } catch (Exception ex) {
                    Log.d(getClass().getName(), ex.getMessage());
                }

                return changedRows;
            }
        };
    }

    /*
    ** Delete
     */
    //todo: delete gebruikt content provider
    public static Observable<Long> delete(Context context, String table, String key, String value) {
        DatabaseHelper connection = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = connection.getWritableDatabase();

        return makeObservable(delete(db, table, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Long> delete(final SQLiteDatabase db, final String table, final String key, final String value) {
        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long changedRows = db.delete(
                        table,
                        key + " = ?",
                        new String[]{value}
                );

                return changedRows;
            }
        };
    }


    /*
    ** General
     */

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            Log.d(TAG, ex.getMessage());
                        }
                    }
                }
        );
    }

    public static Follow cursorToFollow(Cursor cursor){
        Follow follow = new Follow();
        follow.setPageId(cursor.getString(cursor.getColumnIndex(Contract.FollowsColumns.COLUMN_PAGE_ID)));
        follow.setFlag(cursor.getInt(cursor.getColumnIndex(Contract.FollowsColumns.COLUMN_FLAG)));


        return follow;
    }

    public static List<Follow> cursorToFollowList(Cursor cursor){
        List<Follow> follows = new ArrayList<>();
        while (cursor.moveToNext()){
             follows.add(cursorToFollow(cursor));
        }

        return follows;
    }

    public static ContentValues followToContentValuesList(Follow follow) {
        String[] columns = new String[]{
                Contract.FollowsColumns.COLUMN_PAGE_ID,
                Contract.FollowsColumns.COLUMN_FLAG,
        };


        Object[] columnValues = new Object[]{
                follow.getPageId(),
                follow.getFlag()};

        ContentValues contentValues = new ContentValues();
        int counter = 0;
        for (String column : columns) {
            Type type = columnValues[counter].getClass();

            if (type == String.class) {
                contentValues.put(column, (String) columnValues[counter]);
            } else if (type == Integer.class) {
                contentValues.put(column, (Integer) columnValues[counter]);
            } else if (type == Boolean.class) {
                contentValues.put(column, (Boolean) columnValues[counter]);
            } else if (type == Double.class) {
                contentValues.put(column, (Double) columnValues[counter]);
            }

            counter++;
        }
        return contentValues;
    }
}
