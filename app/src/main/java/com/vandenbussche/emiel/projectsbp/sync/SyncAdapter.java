package com.vandenbussche.emiel.projectsbp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.database.PagesAccess;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.database.UploadImagesAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.UploadImage;
import com.vandenbussche.emiel.projectsbp.models.requests.PageRequest;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PageResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import org.apache.commons.io.FileUtils;

import java.io.Console;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Stijn on 8/11/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver contentResolver;
    private SyncResult syncResult;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.contentResolver = context.getContentResolver();
        Log.i("SyncAdapter", "elhow da werkt toch");
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        try {
            Log.i("SyncAdapter","syncProductItems");
            this.syncResult = syncResult;
            syncMyPollsItems(syncResult);
            syncMyPagesItems(syncResult);
            uploadImages();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //http://stackoverflow.com/questions/33821923/how-to-observeon-the-calling-thread-in-java-rx
    private void syncMyPollsItems(final SyncResult syncResult) {
        try {
            // alle polls met newFlag uploaden
            Cursor mData = contentResolver.query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.POLLS_URI, PollsAccess.allColumns, "flag = ?", new String[]{"0"}, null);

            //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
            mData.getCount();

            List<Poll> polls = PollsAccess.cursorToPollList(mData);
            for(final Poll pollLoopItem : polls){
                try {
                    ApiHelper.getApiService(getContext()).saveNewPoll(new PollRequest(pollLoopItem))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<PollResponse>() {
                                @Override
                                public void call(PollResponse pollResponse) {
                                    System.out.println("tis eignelijk gelukt");

                                    List<String> imageUris = new ArrayList<String>();
                                    for(String imageId : pollResponse.getImages()){
                                        imageUris.add(PollResponse.imageIdToUrl(imageId));
                                    }
//                                    Uri updateUri = ContentUris.Contract.POLLS_ITEM_URI, pollLoopItem.get_id());
                                    Uri updateUri = Contract.POLLS_URI;
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB._ID, pollResponse.get_id());
                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.COLUMN_FLAG, Poll.Flags.OK);
                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.COLUMN_UPLOAD_TIME, pollResponse.getUploadTime());
                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.COLUMN_IMAGES, new Gson().toJson(imageUris));

                                    contentResolver.update(updateUri, contentValues, "_id = ?", new String[]{pollLoopItem.get_id()});

                                    syncResult.madeSomeProgress();

                                    if(pollLoopItem.getNumberOfImages() > 0){
                                        pollImagesReadyToUpload(pollLoopItem.get_id(), pollResponse.get_id());
                                        uploadImages();
                                    }
                                }
                            });
                }catch (Exception ex){ex.printStackTrace();}
            }

            // alle polls met updateFlag downloaden


        } catch (Exception ex) {
            ex.printStackTrace();
            syncResult.stats.numIoExceptions++;
            throw ex;
        }
    }

    private void syncMyPagesItems(final SyncResult syncResult) {
        try {
            // alle pages met newFlag uploaden
            Cursor mData = contentResolver.query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.PAGES_URI, PagesAccess.allColumns, "flag = ?", new String[]{"0"}, null);

            //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
            mData.getCount();

            List<Page> pages = PagesAccess.cursorToPageList(mData);
            for(final Page pageLoopItem : pages){
                try {
                    ApiHelper.getApiService(getContext()).saveNewPage(new PageRequest(pageLoopItem))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<PageResponse>() {
                                @Override
                                public void call(PageResponse pageResponse) {
                                    System.out.println("tis eignelijk gelukt");
//                                    Uri updateUri = ContentUris.Contract.PAGES_ITEM_URI, pageLoopItem.get_id());
                                    Uri updateUri = Contract.PAGES_URI;
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB._ID, pageResponse.get_id());
                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.COLUMN_FLAG, Page.Flags.OK);

                                    contentResolver.update(updateUri, contentValues, "_id = ?", new String[]{pageLoopItem.get_id()});

                                    syncResult.madeSomeProgress();
                                }
                            });
                }catch (Exception ex){ex.printStackTrace();}
            }

            // alle pages met updateFlag downloaden


        } catch (Exception ex) {
            ex.printStackTrace();
            syncResult.stats.numIoExceptions++;
            throw ex;
        }
    }

    private void uploadImages(){
        Cursor mData = contentResolver.query(Contract.UPLOAD_IMAGES_URI, UploadImagesAccess.allColumns, "flag = ?", new String[]{UploadImage.Flags.READY_TO_UPLOAD+""}, null);

        //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
        mData.getCount();

        List<UploadImage> uploadImages = UploadImagesAccess.cursorToUploadImageList(mData);

        for (UploadImage uploadImage : uploadImages){
            //todo: set op isUploadingflag
            //todo upload

            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            //File file = FileUtils.getFile( uploadImage.getUrl());
            File file = new File(uploadImage.getUrl());

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(okhttp3.MultipartBody.FORM, file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

            // add another part within the multipart request
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, descriptionString);

            try {
                // finally, execute the request
                ApiHelper.getApiService(getContext()).upload(description, body, uploadImage.getPollId(), uploadImage.getIndex())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String pollResponse) {
                                String s = "wukke dienen";

                            }
                        });

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }

    private void pollImagesReadyToUpload(String oldPollId, String newPollId){
        Uri updateUri = Contract.UPLOAD_IMAGES_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.UploadImagesDB.COLUMN_POLL_ID, newPollId);
        contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.UploadImagesDB.COLUMN_FLAG, UploadImage.Flags.READY_TO_UPLOAD);

        contentResolver.update(updateUri,
                contentValues,
                com.vandenbussche.emiel.projectsbp.database.Contract.UploadImagesDB.COLUMN_POLL_ID+" = ?",
                new String[]{oldPollId});

    }
}
