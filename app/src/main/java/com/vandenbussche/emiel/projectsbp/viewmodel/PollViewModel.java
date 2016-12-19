package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.VotasticApplication;
import com.vandenbussche.emiel.projectsbp.binders.models.PollBinderModel;
import com.vandenbussche.emiel.projectsbp.database.FollowsCache;
import com.vandenbussche.emiel.projectsbp.databinding.RowPollBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PageDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.VoteRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PollUpdateResponse;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

/**
 * Created by emielPC on 11/11/16.
 */
public class PollViewModel {
    RowPollBinding binding;
    PollBinderModel poll;

    LinearLayout optionsLinearLayout;

    List<OptionViewModel> optionViewModels;


    public PollViewModel(ViewGroup parent) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_poll, parent, false);
        optionsLinearLayout = (LinearLayout) binding.getRoot().findViewById(R.id.optionsLinearLayout);
        binding.btnPageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailsOfPage();
            }
        });
        binding.btnFollowPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followPage();
            }
        });
    }

    public void setPoll(Poll poll) {
        this.poll = new PollBinderModel(poll);
        binding.setPoll(this.poll);
        binding.executePendingBindings();

        optionViewModels = new ArrayList<>();

        optionsLinearLayout.removeAllViews();
        int optionLoopnr = 0;
        for (Option option : poll.getOptions()) {
            OptionViewModel ovm = new OptionViewModel(optionsLinearLayout, this);
            ovm.setOption(option);
            if (poll.getChoiceIndex() != -1) {
                int hasVote = poll.getChoiceIndex() == optionLoopnr? 1 : 0;
                ovm.option.setHasVote(hasVote);
            }

            optionsLinearLayout.addView(ovm.getRoot());
            optionViewModels.add(ovm);
            optionLoopnr++;
        }

        updateMaximumVoteInOptions();

        startUpdating();
    }


    public View getRoot() {
        return binding.getRoot();
    }

    public void startUpdating() {
        VotasticApplication.getConnection().emit("join", "poll"+poll.poll.get_id());
        VotasticApplication.getConnection().on("poll" + poll.poll.get_id(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String obj = (String) args[0];
                PollUpdateResponse pollUpdateResponse = new Gson().fromJson(obj, PollUpdateResponse.class);

                if (pollUpdateResponse.getKind().equals("vote")) {
                    addVote(pollUpdateResponse.getVoteResponse().getVoteIndex());//todo index uit args halen
                }
            }
        });
    }

    public void stopUpdating() {
        VotasticApplication.getConnection().emit("leave", "poll"+poll.poll.get_id());
    }

    public void reloadData() {

    }

    public void addVote(int index) {
        //als eerste vote is, mooie animatie maken
        poll.poll.getOptions().get(index).setVotes(poll.poll.getOptions().get(index).getVotes() + 1);
        if (optionViewModels.get(0).option.getMaxVotes() == 0) {
            for (OptionViewModel optionVM :
                    optionViewModels) {
                optionVM.option.setShowPercentage(0);
                optionVM.option.setNewNess(1);
            }
        }
        updateMaximumVoteInOptions();
    }

    public void optionClicked(Option option) {
        VoteRequest voteRequest = new VoteRequest(poll.poll.get_id(), poll.poll.getOptions().indexOf(option));
        VotasticApplication.getConnection().emit(
                "voteSubmit",
                new Gson().toJson(voteRequest));

        addVote(voteRequest.getVoteIndex());

        //alle andere op 0zetten
        for (OptionViewModel optionVM :
                optionViewModels) {
            optionVM.option.setHasVote(optionVM.option.getOption() == option ? 1 : 0);
        }
    }

    public void updateMaximumVoteInOptions() {
        int maxVotes = 0;
        for (Option option : poll.poll.getOptions()) {
            if (option.getVotes() > maxVotes) {
                maxVotes = option.getVotes();
            }
        }

        for (OptionViewModel ovm : optionViewModels) {
            ovm.option.setMaxVotes(maxVotes);
        }
    }

    private void showDetailsOfPage() {
        Intent intent = new Intent(binding.getRoot().getContext(), PageDetailActivity.class);
        intent.putExtra("title", poll.poll.getPageTitle());
        intent.putExtra("_id", poll.poll.getPageId());
        binding.getRoot().getContext().startActivity(intent);
    }

    private void followPage() {
        if (binding.btnFollowPage.getText().toString().toLowerCase().equals("follow")) {
            FollowsCache.addPageId(binding.getRoot().getContext(), poll.poll.getPageId());
            poll.notifyPropertyChanged(BR.following);
            FirebaseMessaging.getInstance().subscribeToTopic("newpollinpage_"+poll.poll.getPageId());
        } else {
            FollowsCache.deletePageId(binding.getRoot().getContext(), poll.poll.getPageId());
            poll.notifyPropertyChanged(BR.following);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("newpollinpage_"+poll.poll.getPageId());
        }

        //todo: send broadcast that a follow changed

    }
}
