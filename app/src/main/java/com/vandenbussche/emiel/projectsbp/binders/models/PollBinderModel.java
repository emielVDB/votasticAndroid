package com.vandenbussche.emiel.projectsbp.binders.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vandenbussche.emiel.projectsbp.database.FollowsCache;
import com.vandenbussche.emiel.projectsbp.models.Poll;

/**
 * Created by emielPC on 11/11/16.
 */
public class PollBinderModel extends BaseObservable{ //wrapper maken voor animaties te vergemakkelijken
    public Poll poll;

    boolean following;

    @Bindable
    public boolean isFollowing() {
        following = FollowsCache.isFolowing(poll.getPageId());
        return following;
    }

    public PollBinderModel(Poll poll){
        this.poll = poll;
    }
}
