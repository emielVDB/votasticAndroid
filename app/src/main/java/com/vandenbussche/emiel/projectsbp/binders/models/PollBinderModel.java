package com.vandenbussche.emiel.projectsbp.binders.models;

import com.vandenbussche.emiel.projectsbp.models.Poll;

/**
 * Created by emielPC on 11/11/16.
 */
public class PollBinderModel { //wrapper maken voor animaties te vergemakkelijken
    public Poll poll;
    // later.. isFromFollowedPage

    public PollBinderModel(Poll poll){
        this.poll = poll;
    }
}
