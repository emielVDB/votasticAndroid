package com.vandenbussche.emiel.projectsbp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandenbussche.emiel.projectsbp.models.UploadImage;

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
public class UploadImagesAccess {

    public static final String TAG = "UploadImagesAccess";

    public static final String[] allColumns = new String[]{
            Contract.UploadImagesColumns._ID,
            Contract.UploadImagesColumns.COLUMN_POLL_ID,
            Contract.UploadImagesColumns.COLUMN_INDEX,
            Contract.UploadImagesColumns.COLUMN_URL,
            Contract.UploadImagesColumns.COLUMN_FLAG,
    };


     /*
    ** Get all
     */

    public static Observable<List<UploadImage>> getAll(Context context) {

        return makeObservable(getAllCallable(context))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<UploadImage>> getAllCallable(final Context context) {
        return new Callable<List<UploadImage>>() {
            @Override
            public List<UploadImage> call() throws Exception {



                Cursor mData = context.getContentResolver().query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.UPLOAD_IMAGES_URI, allColumns, null, null, null);

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToUploadImageList(mData);
            }
        };
    }



      /*
    ** GetByKey
     */

    public static Observable<List<UploadImage>> getByKey(Context context, String key, String value) {


        return makeObservable(getByKeyCallable(context, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<List<UploadImage>> getByKeyCallable(final Context context, final String key, final String value) {
        return new Callable<List<UploadImage>>() {
            @Override
            public List<UploadImage> call() throws Exception {


                Cursor mData = context.getContentResolver().query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.UPLOAD_IMAGES_URI, allColumns,  key + " = ?", new String[]{value}, null);

                //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
                mData.getCount();

                return cursorToUploadImageList(mData);
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

                context.getContentResolver().insert(com.vandenbussche.emiel.projectsbp.database.provider.Contract.UPLOAD_IMAGES_URI, values);

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
                    context.getContentResolver().update(com.vandenbussche.emiel.projectsbp.database.provider.Contract.UPLOAD_IMAGES_URI, values, key + " = ?", new String[]{value});

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
    public static Observable<Long> delete(Context context, String key, String value) {
        DatabaseHelper connection = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = connection.getWritableDatabase();

        return makeObservable(deleteCallable(context, key, value))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Long> deleteCallable(final Context context, final String key, final String value) {
        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                context.getContentResolver().delete(com.vandenbussche.emiel.projectsbp.database.provider.Contract.UPLOAD_IMAGES_URI, key + " = ?", new String[]{value});

                return 0L;
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

    public static UploadImage cursorToUploadImage(Cursor cursor){
        UploadImage uploadImage = new UploadImage();
        uploadImage.set_id(cursor.getInt(cursor.getColumnIndex(Contract.UploadImagesDB._ID)));
        uploadImage.setPollId(cursor.getString(cursor.getColumnIndex(Contract.UploadImagesColumns.COLUMN_POLL_ID)));
        uploadImage.setIndex(cursor.getInt(cursor.getColumnIndex(Contract.UploadImagesColumns.COLUMN_INDEX)));
        uploadImage.setUrl(cursor.getString(cursor.getColumnIndex(Contract.UploadImagesColumns.COLUMN_URL)));
        uploadImage.setFlag(cursor.getInt(cursor.getColumnIndex(Contract.UploadImagesColumns.COLUMN_FLAG)));

        return uploadImage;
    }

    public static List<UploadImage> cursorToUploadImageList(Cursor cursor){
        List<UploadImage> uploadImages = new ArrayList<>();
        while (cursor.moveToNext()){
             uploadImages.add(cursorToUploadImage(cursor));
        }

        return uploadImages;
    }

    public static ContentValues uploadImageToContentValuesList(UploadImage uploadImage) {
        String[] columns = new String[]{
                Contract.UploadImagesColumns.COLUMN_POLL_ID,
                Contract.UploadImagesColumns.COLUMN_INDEX,
                Contract.UploadImagesColumns.COLUMN_URL,
                Contract.UploadImagesColumns.COLUMN_FLAG,
        };


        Object[] columnValues = new Object[]{
                uploadImage.getPollId(),
                uploadImage.getIndex(),
                uploadImage.getUrl(),
                uploadImage.getFlag()};

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
