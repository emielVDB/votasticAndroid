package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityNewPollBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ContentNewPollBinding;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 16/11/16.
 */
public class NewPollActivityViewModel {
    ActivityNewPollBinding binding;
    Context context;

    public NewPollActivityViewModel(final ActivityNewPollBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
        binding.content.btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add tag
                if(binding.getPoll().getTags() == null){
                    binding.getPoll().setTags(new ObservableArrayList<String>());
                }

                binding.getPoll().getTags().add(0, binding.content.txtTag.getText().toString());
                binding.content.txtTag.setText("");
            }
        });

        binding.content.btnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add options
                if(binding.getPoll().getOptions() == null){
                    binding.getPoll().setOptions(new ObservableArrayList<String>());
                }

                binding.getPoll().getOptions().add(0, binding.content.txtOption.getText().toString());
                binding.content.txtOption.setText("");
            }
        });

        binding.content.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPoll();
            }
        });
    }

    private void uploadPoll() {
        final Poll poll = binding.getPoll().toPoll();

        PollsAccess.insert(context, PollsAccess.pollToContentValuesList(poll))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                    Account account = AuthHelper.getAccount(context);
                    while (ContentResolver.isSyncPending(account, Contract.AUTHORITY)  ||
                            ContentResolver.isSyncActive(account, Contract.AUTHORITY)) {
                        Log.i("ContentResolver", "SyncPending, canceling");
                        ContentResolver.cancelSync(account, Contract.AUTHORITY);
                    }

                    Bundle settingsBundle = new Bundle();
                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    context.getContentResolver().requestSync(account,
                            Contract.AUTHORITY, settingsBundle);
                    }
                });



    }
}
