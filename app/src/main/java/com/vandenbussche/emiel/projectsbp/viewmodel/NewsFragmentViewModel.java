package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.IncrementalPollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentNewsBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.RandomPollsActivity;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollList;
import com.vandenbussche.emiel.projectsbp.models.Reaction;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by emielPC on 10/11/16.
 */
public class NewsFragmentViewModel extends BaseObservable implements IncrementalPollsAdaptarWithHeader.NewPollsNeededListener,
        PollsAdaptarWithHeader.PollsAdapterWithHeaderListener{
    IncrementalPollsAdaptarWithHeader adapter = null;


    FragmentNewsBinding binding;
    public NewsFragmentViewModel(FragmentNewsBinding binding){
        this.binding = binding;
    }

    public void loadPolls() {
        loadPolls(0);
    }

    public void loadPolls(long maxUploadTime){
        ApiHelper.getApiService(binding.getRoot().getContext()).getNewsPolls( maxUploadTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PollResponse>>() {
                    @Override
                    public void call(List<PollResponse> pollResponses) {
                        if(pollResponses.size() == 0) return;
                        List<Poll> pollList = new ArrayList<Poll>();
                        for(PollResponse pollResponse : pollResponses){
                            pollList.add(pollResponse.toPoll());
                        }
                        if(adapter == null) {
                            adapter = new IncrementalPollsAdaptarWithHeader(pollList, binding.getRoot().getContext(),
                                    NewsFragmentViewModel.this, NewsFragmentViewModel.this, R.layout.header_random_polls_activity);
                            binding.pollsRecyclerView.setAdapter(adapter);
                        }else{
                            adapter.addPolls(pollList);
                        }
                        binding.setDataLoaded(true);
                        notifyPropertyChanged(BR.dataLoaded);
                    }
                });
    }

    @Override
    public void getNewPolls(Poll lastPoll) {
        loadPolls(lastPoll.getUploadTime() - 1);
    }

    @Override
    public void onBindHeaderView(View v) {

    }
}
