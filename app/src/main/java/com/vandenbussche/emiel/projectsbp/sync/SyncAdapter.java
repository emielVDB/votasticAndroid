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
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.io.Console;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
            Log.i("SyncAdapter","syncProductItems");
            Log.i("SyncAdapter","syncProductItems");
            Log.i("SyncAdapter","syncProductItems");
            Log.i("SyncAdapter","syncProductItems");

            this.syncResult = syncResult;
            syncMyPollsItems(syncResult);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //http://stackoverflow.com/questions/33821923/how-to-observeon-the-calling-thread-in-java-rx
    private void syncMyPollsItems(final SyncResult syncResult) {
        try {
            //todo: tzelfde voor de pages doen als hieronder
            // alle polls met newFlag uploaden
            Cursor mData = contentResolver.query(com.vandenbussche.emiel.projectsbp.database.provider.Contract.POLLS_URI, PollsAccess.allColumns, "flag = ?", new String[]{"0"}, null);

            //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
            mData.getCount();

            List<Poll> polls = PollsAccess.cursorToPollList(mData);
            for(final Poll pollLoopItem : polls){
                try {
                    System.out.println("gooooooeeeee  beeeeezziiiiiiiggggg");
                    ApiHelper.getApiService(getContext()).saveNewPoll(new PollRequest(pollLoopItem))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<PollResponse>() {
                                @Override
                                public void call(PollResponse pollResponse) {
                                    System.out.println("tis eignelijk gelukt");
                                }
                            });

//                    ApiHelper.getApiService(getContext()).saveNewPoll(new PollRequest(pollLoopItem))
//                            .subscribeOn(Schedulers.io())
//                            .subscribe(new Action1<PollResponse>() {
//                                @Override
//                                public void call(PollResponse pollResponse) {
//
//                                    //id updaten, en flag op ok zetten
////                                Uri updateUri = ContentUris.Contract.POLLS_ITEM_URI, pollLoopItem.get_id());
//                                    Uri updateUri = Contract.POLLS_URI;
//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB._ID, pollResponse.get_id());
//                                    contentValues.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.COLUMN_FLAG, pollResponse.getFlag());
//
//                                    contentResolver.update(updateUri, contentValues, "_id = ?", new String[]{pollLoopItem.get_id()});
//
//                                    syncResult.madeSomeProgress();
//                                }
//                            });
                }catch (Exception ex){ex.printStackTrace();}
            }

            // alle polls met updateFlag downloaden


        } catch (Exception ex) {
            ex.printStackTrace();
            syncResult.stats.numIoExceptions++;
            throw ex;
        }
    }
}
