package com.vandenbussche.emiel.projectsbp.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.vandenbussche.emiel.projectsbp.DoneListener;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.models.Follow;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 1/12/16.
 */

public class FollowsCache {

    private static List<Follow> follows;

    public FollowsCache(){}

    public static List<Follow> getFollows() {
        if(follows == null) throw new NullPointerException("loadAllFollows nooit opgeroepen");
        return follows;
    }

    public static void addPageId(final Context context, String pageId){
        final Follow follow = new Follow();
        follow.setPageId(pageId);
        follow.setFlag(Follow.Flags.UPLOAD_ADD);
        follows.add(follow);
        FollowsAccess.insert(context, FollowsAccess.followToContentValuesList(follow))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                });

        ApiHelper.getApiService(context).addFollow(follow.getPageId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        follow.setFlag(Follow.Flags.OK);
                        updatePollInDatabase(context, follow);
                    }
                });
    }

    private static void updatePollInDatabase(Context context, Follow follow) {
        FollowsAccess.update(context, FollowsAccess.followToContentValuesList(follow), Contract.FollowsDB.COLUMN_PAGE_ID, follow.getPageId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                });
    }

    public static void loadAllFollows(Context context ,final DoneListener listener){
        FollowsAccess.getAll(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Follow>>() {
                    @Override
                    public void call(final List<Follow> follows) {
                        FollowsCache.follows = follows;
                        listener.done();
                    }
                });
    }

    public static boolean isFolowing(String pageId) {
        if(pageId == null) return false;
        for(Follow follow : getFollows()){
            if(follow.getPageId().equals(pageId)){
                return true;
            }
        }

        return false;
    }
}
