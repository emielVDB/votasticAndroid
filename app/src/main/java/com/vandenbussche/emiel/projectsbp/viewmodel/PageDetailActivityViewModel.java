package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.IncrementalPollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.PagesAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.database.Contract;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PageDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollList;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 29/11/16.
 */

public class PageDetailActivityViewModel extends BaseObservable implements PollsAdaptarWithHeader.PollsAdapterWithHeaderListener,
        IncrementalPollsAdaptarWithHeader.NewPollsNeededListener {
    IncrementalPollsAdaptarWithHeader adapter;
    ActivityPageDetailBinding binding;
    Context context;
    String pageId;
    boolean isFromServer = false;

    public PageDetailActivityViewModel(ActivityPageDetailBinding binding, Context context) {
        this.context = context;
        this.binding = binding;
    }


    public void loadPolls(final String pageId){
        this.pageId = pageId;
        PollsAccess.getByKey(context, Contract.PollsColumns.COLUMN_PAGE_ID, pageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Poll>>() {
                    @Override
                    public void call(List<Poll> polls) {
                        if(polls.size() == 0){
                            loadPollsFromServer(pageId);
                            return;
                        }

                        PollsAdaptarWithHeader adapter = new PollsAdaptarWithHeader(polls, context, PageDetailActivityViewModel.this, R.layout.header_page_detail);
                        binding.content.pollsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                });
    }
    public void loadPollsFromServer(String pageId){
        isFromServer = true;
        loadPollsFromServer(pageId, 0);
    }
    public void loadPollsFromServer(String pageId, long maxUploadTime){
        ApiHelper.getApiService(binding.getRoot().getContext()).getPollsByPageId(pageId, maxUploadTime)
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
                                    PageDetailActivityViewModel.this, PageDetailActivityViewModel.this, R.layout.header_page_detail);
                            binding.content.pollsRecyclerView.setAdapter(adapter);
                        }else{
                            adapter.addPolls(pollList);
                        }
                        binding.content.setDataLoaded(true);
                        notifyPropertyChanged(BR.dataLoaded);
                    }
                });
    }

    @Override
    public void onBindHeaderView(View v) {

    }

    @Override
    public void getNewPolls(Poll lastPoll) {
        loadPollsFromServer(this.pageId, lastPoll.getUploadTime() - 1);
    }
}
