package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.gui.fragments.BlankFragment;
import com.vandenbussche.emiel.projectsbp.gui.fragments.ProfileMyPagesFragment;
import com.vandenbussche.emiel.projectsbp.gui.fragments.ProfileProfileFragment;
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
public class ProfileMyPollsFragmentViewModel extends BaseObservable implements PollsAdaptarWithHeader.PollsAdapterWithHeaderListener{
    private FragmentProfileMyPollsBinding binding;
    private Context context;
    private FragmentManager fragmentManager;

    public ProfileMyPollsFragmentViewModel(FragmentProfileMyPollsBinding binding, Context context, FragmentManager fragmentManager){
        this.binding = binding;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void loadPolls() {
        PollsAccess.getAll(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Poll>>() {
                    @Override
                    public void call(List<Poll> polls) {
                        PollList pollList = new PollList();
                        pollList.setData(polls);
                        binding.setAdapterListener(ProfileMyPollsFragmentViewModel.this);
                        binding.setPollList(pollList);
                        notifyPropertyChanged(BR.pollList);
                  }
                });
    }

    @Override
    public void onBindHeaderView(View v) {
        Button btnMyPolls = (Button)v.findViewById(R.id.btnProfileMyPolls);
        btnMyPolls.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimary) , PorterDuff.Mode.MULTIPLY);

        v.findViewById(R.id.btnProfileMyPages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frmContent, new ProfileMyPagesFragment()).commit();
            }
        });
        v.findViewById(R.id.btnProfileProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frmContent, new ProfileProfileFragment()).commit();
            }
        });
    }
}
