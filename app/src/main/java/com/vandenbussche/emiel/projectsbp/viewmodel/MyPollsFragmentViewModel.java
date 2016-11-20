package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.util.Log;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollList;
import com.vandenbussche.emiel.projectsbp.models.Reaction;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by emielPC on 10/11/16.
 */
public class MyPollsFragmentViewModel extends BaseObservable{
    FragmentMyPollsBinding binding;
    Context context;

    public MyPollsFragmentViewModel(FragmentMyPollsBinding binding, Context context){
        this.binding = binding;
        this.context = context;
    }

    public void loadPolls() {
        PollsAccess.getAll(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Poll>>() {
                    @Override
                    public void call(List<Poll> polls) {
                        PollList pollList = new PollList();
                        pollList.setData(polls);
                        binding.setPollList(pollList);
                        notifyPropertyChanged(BR.pollList);
                    }
                });
    }
}
