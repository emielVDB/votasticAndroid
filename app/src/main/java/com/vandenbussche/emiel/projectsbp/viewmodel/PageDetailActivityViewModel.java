package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.database.Contract;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PageDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollList;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by emielPC on 29/11/16.
 */

public class PageDetailActivityViewModel extends BaseObservable implements PollsAdaptarWithHeader.PollsAdapterWithHeaderListener  {
    ActivityPageDetailBinding binding;
    Context context;
    String pageId;

    public PageDetailActivityViewModel(ActivityPageDetailBinding binding, Context context) {
        this.context = context;
        this.binding = binding;
    }


    public void loadPolls(String pageId){
        this.pageId = pageId;
        PollsAccess.getByKey(context, Contract.PollsColumns.COLUMN_PAGE_ID, pageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Poll>>() {
                    @Override
                    public void call(List<Poll> polls) {

                        PollList pollList = new PollList();
                        pollList.setData(polls);

                        binding.setAdapterListener(PageDetailActivityViewModel.this);
                        binding.setPollList(pollList);
                        notifyPropertyChanged(BR.pollList);
                    }
                });
    }

    @Override
    public void onBindHeaderView(View v) {

    }
}
