package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.VotasticApplication;
import com.vandenbussche.emiel.projectsbp.adapters.IncrementalReactionsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.ReactionsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPollDetailBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PollDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.Reaction;
import com.vandenbussche.emiel.projectsbp.models.requests.ReactionRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.AddReactionResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollUpdateResponse;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 28/12/16.
 */

public class PollDetailActivityViewModel implements IncrementalReactionsAdaptarWithHeader.NewReactionsNeededListener, ReactionsAdaptarWithHeader.ReactionsAdapterWithHeaderListener {
    Poll poll;
    ActivityPollDetailBinding binding;
    PollDetailActivity activity;
    LinearLayoutManager linearLayoutManager;

    IncrementalReactionsAdaptarWithHeader adaptar;

    public PollDetailActivityViewModel(ActivityPollDetailBinding binding,
                                       final PollDetailActivity activity,
                                       Poll poll,
                                       LinearLayoutManager linearLayoutManager) {
        this.poll = poll;
        this.binding = binding;
        this.activity = activity;
        this.linearLayoutManager = linearLayoutManager;

        binding.content.btnSendReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReaction();
            }
        });

        showHeader();
        //loadReactions();

        //VotasticApplication.getConnection().emit("join", "poll"+poll.get_id());
        VotasticApplication.getConnection().on("poll" + poll.get_id(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String obj = (String) args[0];
                PollUpdateResponse pollUpdateResponse = new Gson().fromJson(obj, PollUpdateResponse.class);

                if (pollUpdateResponse.getKind().equals("addReaction")) {
                    System.out.println("waaaawieeeeee");
                    final AddReactionResponse addReactionResponse = pollUpdateResponse.getAddReactionResponse();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adaptar.addNewReaction(addReactionResponse.toReaction());
                        }
                    });

                }
            }
        });
    }

    private void sendReaction() {
        String content = binding.content.txtReaction.getText().toString();

        VotasticApplication.getConnection().emit(
                "reactionSubmit",
                new Gson().toJson(new ReactionRequest(poll.get_id(), content)));

//        adaptar.addNewReaction(new Reaction(content, System.currentTimeMillis()));

        linearLayoutManager.scrollToPositionWithOffset(1, 80);

        binding.content.txtReaction.setText("");
    }

    private void showHeader() {
        this.adaptar = new IncrementalReactionsAdaptarWithHeader(new ArrayList<Reaction>(),
                binding.getRoot().getContext(),
                this, this, R.layout.header_detail_poll);
        binding.content.reactionsRecyclerView.setAdapter(adaptar);
    }

    public void loadReactions(long maxUploadTime){
        ApiHelper.getApiService(binding.getRoot().getContext()).getReactions(poll.get_id(), maxUploadTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Reaction>>() {
            @Override
            public void call(List<Reaction> reactionResponseList) {
                adaptar.addReactions(reactionResponseList);
            }
        });

    }

    @Override
    public void getNewReactions(Reaction lastReaction) {
        if(lastReaction == null){
            loadReactions(0);
        }else{
            loadReactions(lastReaction.getUploadTime() - 1);
        }
    }

    @Override
    public void onBindHeaderView(View v, boolean isFirstTime) {
        new PollDetailViewModel(v).setPoll(poll, isFirstTime);
    }
}
