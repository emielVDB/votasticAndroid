package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.PollImagesAdaptar;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.PagesAccess;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.database.UploadImagesAccess;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityNewPollBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ContentNewPollBinding;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.UploadImage;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
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
    NewPollViewmodelListener listener;

    PollImagesAdaptar imagesAdaptar = null;
    ObservableList<String> images = new ObservableArrayList<>();


    public NewPollActivityViewModel(final ActivityNewPollBinding binding, Context context, NewPollViewmodelListener listener) {
        this.binding = binding;
        this.context = context;
        this.listener = listener;
        imagesAdaptar = new PollImagesAdaptar(images, context, true);
        binding.content.imagesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.content.imagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.content.imagesRecyclerView.setAdapter(imagesAdaptar);
        binding.content.btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add tag
                if (binding.getPoll().getTags() == null) {
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
                if (binding.getPoll().getOptions() == null) {
                    binding.getPoll().setOptions(new ObservableArrayList<String>());
                }

                binding.getPoll().getOptions().add(0, binding.content.txtOption.getText().toString());
                binding.content.txtOption.setText("");
            }
        });

        binding.content.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonClicked();
            }
        });
        binding.content.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageButtonClicked();
            }
        });
    }

    private void addImageButtonClicked() {
        CharSequence colors[] = new CharSequence[] {"Take picture", "From gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add image");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                if(which == 0){
                    listener.startTakePictureIntent();
                }else if(which == 1){
                    listener.startLoadPictureIntent();
                }
            }
        });
        builder.show();
    }

    private void AddImage(){
        listener.startTakePictureIntent();
    }

    public void imageUrlAdded(String url){
        images.add(url);
    }

    private void doneButtonClicked(){
        PagesAccess.getAll(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Page>>() {
                    @Override
                    public void call(final List<Page> pages) {
                        if(pages.size() == 0){ uploadPoll(); return;}

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setMessage(R.string.addToPoll)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //ask wich one
                                        showChoosePageDialog(pages);
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        uploadPoll();
                                    }
                                });
                        // Create the AlertDialog object and return it
                        builder.create().show();
                    }
                });
    }

    private void showChoosePageDialog(final List<Page> pages) {
        String[] strPages = new String[pages.size()];
        int loopnr = 0;
        for (Page page : pages) {
            strPages[loopnr] = page.getTitle();
            loopnr++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choosePage)
        .setItems(strPages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.getPoll().setPageId(pages.get(which).get_id());

                final Poll poll = binding.getPoll().toPoll();
                poll.setPageTitle(pages.get(which).getTitle());
                uploadPoll(poll);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                uploadPoll();
            }
        });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void uploadPoll(){
        saveImagesInUploadDB();
    }

    int imagesLeftToUpload = 0;
    private void saveImagesInUploadDB(){
        final Poll poll = binding.getPoll().toPoll();
        poll.setNumberOfImages(images.size());

        if(images.size() == 0) {
            uploadPoll(poll);
            return;
        }

        imagesLeftToUpload = images.size();
        int imageLoopnr = 0;
        for(String imageUrl : images){
            UploadImage uploadImage = new UploadImage();
            uploadImage.setPollId(poll.get_id());
            uploadImage.setIndex(imageLoopnr);
            uploadImage.setUrl(imageUrl);
            uploadImage.setFlag(UploadImage.Flags.NEW);

            UploadImagesAccess.insert(context, UploadImagesAccess.uploadImageToContentValuesList(uploadImage))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            imagesLeftToUpload--;
                            if(imagesLeftToUpload == 0) {
                                uploadPoll(poll);
                            }
                        }

                    });


            imageLoopnr++;
        }
    }
    private void uploadPoll(Poll poll) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving poll...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        PollsAccess.insert(context, PollsAccess.pollToContentValuesList(poll))
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

    public interface NewPollViewmodelListener{
        public void startTakePictureIntent();
        public void startLoadPictureIntent();
    }
}
