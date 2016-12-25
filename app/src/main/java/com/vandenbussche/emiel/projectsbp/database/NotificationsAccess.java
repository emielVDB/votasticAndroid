package com.vandenbussche.emiel.projectsbp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
public class NotificationsAccess {

    public static final String TAG = "NotificationsAccess";

    public static final String[] allColumns = new String[]{
            Contract.NotificationsColumns._ID,
            Contract.NotificationsColumns.COLUMN_MESSAGE,
    };


     /*
    ** Get all
     */

    public static Observable<List<ContentValues>> getAll(Context context) {

        return makeObservable(getAllCallable(context))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<ContentValues>> getAllCallable(final Context context) {
        return new Callable<List<ContentValues>>() {
            @Override
            public List<ContentValues> call() throws Exception {



                Cursor mData = context.getContentResolver().query(
                        com.vandenbussche.emiel.projectsbp.database.provider.Contract.NOTIFICATIONS_URI,
                        allColumns,
                        null,
                        null,
                        Contract.NotificationsDB._ID+" DESC");

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToNotificationList(mData);
            }
        };
    }



      /*
    ** GetByKey
     */

    public static Observable<List<ContentValues>> getFive(Context context, String key, String value) {


        return makeObservable(getByKeyCallable(context, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<ContentValues>> getByKeyCallable(final Context context, final String key, final String value) {
        return new Callable<List<ContentValues>>() {
            @Override
            public List<ContentValues> call() throws Exception {

                Cursor mData = context.getContentResolver().query(
                        com.vandenbussche.emiel.projectsbp.database.provider.Contract.NOTIFICATIONS_URI,
                        allColumns,
                        key + " < ?",
                        new String[]{value},
                        Contract.NotificationsDB._ID+" DESC");

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToNotificationList(mData);
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

                context.getContentResolver().insert(com.vandenbussche.emiel.projectsbp.database.provider.Contract.NOTIFICATIONS_URI, values);

                return 0l;
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

    public static ContentValues cursorToNotification(Cursor cursor){
        ContentValues contentValues = new ContentValues();
        contentValues.put("message", cursor.getString(cursor.getColumnIndex(Contract.NotificationsColumns.COLUMN_MESSAGE)));
        contentValues.put("_id", cursor.getInt(cursor.getColumnIndex(Contract.NotificationsColumns._ID)));
        return contentValues;

    }

    public static List<ContentValues> cursorToNotificationList(Cursor cursor){
        List<ContentValues> notifications = new ArrayList<>();
        while (cursor.moveToNext()){
             notifications.add(cursorToNotification(cursor));
        }

        return notifications;
    }

    public static ContentValues notificationToContentValuesList(String notification) {
        String[] columns = new String[]{
                Contract.NotificationsColumns.COLUMN_MESSAGE,
        };


        Object[] columnValues = new Object[]{
                notification};

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
