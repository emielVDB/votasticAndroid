package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.PagesAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityNewPageBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityNewPageBinding;
import com.vandenbussche.emiel.projectsbp.models.Page;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 16/11/16.
 */
public class NewPageActivityViewModel {
    ActivityNewPageBinding binding;
    Context context;

    public NewPageActivityViewModel(final ActivityNewPageBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
        binding.content.btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add tag
                if (binding.getPage().getTags() == null) {
                    binding.getPage().setTags(new ObservableArrayList<String>());
                }

                binding.getPage().getTags().add(0, binding.content.txtTag.getText().toString());
                binding.content.txtTag.setText("");
            }
        });

        binding.content.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPage();
            }
        });
    }

    private void uploadPage() {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving poll...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        final Page poll = binding.getPage().toPage();

        PagesAccess.insert(context, PagesAccess.pollToContentValuesList(poll))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Account account = AuthHelper.getAccount(context);
                        while (ContentResolver.isSyncPending(account, Contract.AUTHORITY) ||
                                ContentResolver.isSyncActive(account, Contract.AUTHORITY)) {
                            Log.i("ContentResolver", "SyncPending, canceling");
                            ContentResolver.cancelSync(account, Contract.AUTHORITY);
                        }

                        Bundle settingsBundle = new Bundle();
                        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                        context.getContentResolver().requestSync(account,
                                Contract.AUTHORITY, settingsBundle);

                        pDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
    }
}
