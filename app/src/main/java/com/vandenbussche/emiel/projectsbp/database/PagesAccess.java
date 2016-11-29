package com.vandenbussche.emiel.projectsbp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.Reaction;

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
public class PagesAccess {

    public static final String TAG = "PollsAccess";

    public static final String[] allColumns = new String[]{
            Contract.PagesColumns._ID,
            Contract.PagesColumns.COLUMN_TITLE,
            Contract.PagesColumns.COLUMN_TAGS,
            Contract.PagesColumns.COLUMN_POLLS_COUNT,
            Contract.PagesColumns.COLUMN_FLAG,
    };


     /*
    ** Get all
     */

    public static Observable<List<Page>> getAll(Context context) {

        return makeObservable(getAllCallable(context))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<Page>> getAllCallable(final Context context) {
        return new Callable<List<Page>>() {
            @Override
            public List<Page> call() throws Exception {



                Cursor mData = context.getContentResolver().query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.PAGES_URI, allColumns, null, null, null);

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToPageList(mData);
            }
        };
    }



      /*
    ** GetByKey
     */

    public static Observable<List<Page>> getByKey(Context context, String key, String value) {


        return makeObservable(getByKeyCallable(context, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<Page>> getByKeyCallable(final Context context, final String key, final String value) {
        return new Callable<List<Page>>() {
            @Override
            public List<Page> call() throws Exception {


                Cursor mData = context.getContentResolver().query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.PAGES_URI, allColumns,  key + " = ?", new String[]{value}, null);

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToPageList(mData);
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

                context.getContentResolver().insert(com.vandenbussche.emiel.projectsbp.database.provider.Contract.PAGES_URI, values);

                return 0l;
            }
        };
    }


     /*
    ** Update
     */

    //todo: content provider gebruiken
    public static Observable<Long> update(Context context, String table, String[] columns, String[] columnValues, String key, String value) {
        DatabaseHelper connection = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = connection.getWritableDatabase();

        return makeObservable(update(db, table, columns, columnValues, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Long> update(final SQLiteDatabase db, final String table, final String[] columns, final Object[] columnValues,
                                         final String key, final String value) {
        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long changedRows = -1;

                ContentValues values = new ContentValues();
                try {
                    int counter = 0;
                    for (String column : columns) {
                        Type type = columnValues[counter].getClass();

                        if (type == String.class) {
                            values.put(column, (String) columnValues[counter]);
                        } else if (type == Integer.class) {
                            values.put(column, (Integer) columnValues[counter]);
                        } else if (type == Boolean.class) {
                            values.put(column, (Boolean) columnValues[counter]);
                        } else if (type == Double.class) {
                            values.put(column, (Double) columnValues[counter]);
                        }

                        counter++;
                    }

                    changedRows = db.update(
                            table,
                            values,
                            key + " = ?",
                            new String[]{value}
                    );
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

    public static Page cursorToPage(Cursor cursor){
        Page page = new Page();
        page.set_id(cursor.getString(cursor.getColumnIndex(Contract.PagesColumns._ID)));
        page.setTitle(cursor.getString(cursor.getColumnIndex(Contract.PagesColumns.COLUMN_TITLE)));
        page.setFlag(cursor.getInt(cursor.getColumnIndex(Contract.PagesColumns.COLUMN_FLAG)));
        page.setPollsCount(cursor.getInt(cursor.getColumnIndex(Contract.PagesColumns.COLUMN_POLLS_COUNT)));

        List<String> tags = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(Contract.PagesColumns.COLUMN_TAGS)),
                new TypeToken<ArrayList<String>>() {}.getType());//list<String> type
        page.setTags(tags);

        return page;
    }

    public static List<Page> cursorToPageList(Cursor cursor){
        List<Page> pages = new ArrayList<>();
        while (cursor.moveToNext()){
             pages.add(cursorToPage(cursor));
        }

        return pages;
    }

    public static ContentValues pageToContentValuesList(Page page) {
        String[] columns = new String[]{
                Contract.PagesColumns._ID,
                Contract.PagesColumns.COLUMN_TITLE,
                Contract.PagesColumns.COLUMN_TAGS,
                Contract.PagesColumns.COLUMN_POLLS_COUNT,
                Contract.PagesColumns.COLUMN_FLAG,
        };


        Object[] columnValues = new Object[]{
                page.get_id(),
                page.getTitle(),
                new Gson().toJson(page.getTags()),
                page.getPollsCount(),
                page.getFlag()};

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
