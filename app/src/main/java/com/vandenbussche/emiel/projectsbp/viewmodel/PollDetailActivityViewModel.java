package com.vandenbussche.emiel.projectsbp.viewmodel;

import com.vandenbussche.emiel.projectsbp.databinding.ActivityPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPollDetailBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PollDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Poll;

/**
 * Created by emielPC on 28/12/16.
 */

public class PollDetailActivityViewModel {
    Poll poll;
    ActivityPollDetailBinding binding;
    PollDetailActivity activity;

    public PollDetailActivityViewModel(ActivityPollDetailBinding binding, PollDetailActivity activity, Poll poll) {
        this.poll = poll;
        this.binding = binding;
        this.activity = activity;

        loadReactions();
    }

    public void loadReactions(){

    }
}
