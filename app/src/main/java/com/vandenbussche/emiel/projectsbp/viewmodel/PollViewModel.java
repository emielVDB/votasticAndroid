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
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.binders.models.PollBinderModel;
import com.vandenbussche.emiel.projectsbp.database.Contract;
import com.vandenbussche.emiel.projectsbp.database.FollowsCache;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.databinding.RowPollBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PageDetailActivity;
import com.vandenbussche.emiel.projectsbp.gui.activities.PollDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollBindable;
import com.vandenbussche.emiel.projectsbp.models.requests.VoteRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollUpdateResponse;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 11/11/16.
 */
public class PollViewModel extends PollBindable{

    public PollViewModel(ViewGroup parent) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_poll, parent, false);
        optionsLinearLayout = (LinearLayout) binding.getRoot().findViewById(R.id.optionsLinearLayout);
        ((RowPollBinding)binding).btnPageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailsOfPage();
            }
        });
        ((RowPollBinding)binding).btnFollowPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followPage();
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPollDetails();
            }
        });
    }

    private void showPollDetails(){
        Intent intent = new Intent(binding.getRoot().getContext(), PollDetailActivity.class);
        intent.putExtra("poll", poll.poll);
        binding.getRoot().getContext().startActivity(intent);
    }

    private void showDetailsOfPage() {
        Intent intent = new Intent(binding.getRoot().getContext(), PageDetailActivity.class);
        intent.putExtra("title", poll.poll.getPageTitle());
        intent.putExtra("_id", poll.poll.getPageId());
        binding.getRoot().getContext().startActivity(intent);
    }

    private void followPage() {
        if (((RowPollBinding)binding).btnFollowPage.getText().toString().toLowerCase().equals("follow")) {
            FollowsCache.addPageId(binding.getRoot().getContext(), poll.poll.getPageId());
            poll.notifyPropertyChanged(BR.following);
        } else {
            FollowsCache.deletePageId(binding.getRoot().getContext(), poll.poll.getPageId());
            poll.notifyPropertyChanged(BR.following);
        }

        //todo: send broadcast that a follow changed

    }

    @Override
    protected void bindingSetPoll(PollBinderModel poll) {
        ((RowPollBinding)binding).setPoll(poll);
    }
}
