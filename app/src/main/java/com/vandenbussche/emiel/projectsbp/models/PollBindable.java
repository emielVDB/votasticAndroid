package com.vandenbussche.emiel.projectsbp.models;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.vandenbussche.emiel.projectsbp.VotasticApplication;
import com.vandenbussche.emiel.projectsbp.adapters.PollImagesAdaptar;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.binders.models.PollBinderModel;
import com.vandenbussche.emiel.projectsbp.database.Contract;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.models.requests.VoteRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollUpdateResponse;
import com.vandenbussche.emiel.projectsbp.viewmodel.OptionViewModel;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 28/12/16.
 */

public abstract class PollBindable {
    public ViewDataBinding binding;

    public PollBinderModel poll;

    public LinearLayout optionsLinearLayout;
    public RecyclerView imagesRecycler;

    public List<OptionViewModel> optionViewModels;

    public void setPoll(Poll poll) {
        setPoll(poll, true);
    }
    public void setPoll(Poll poll, boolean animate){
        this.poll = new PollBinderModel(poll);


        //images
        if(poll.getImages() != null && poll.getImages().size() > 0) {
            ObservableList<String> observableImages = new ObservableArrayList<>();
            observableImages.addAll(poll.getImages());
            PollImagesAdaptar imagesAdaptar = new PollImagesAdaptar(observableImages,
                    binding.getRoot().getContext(), false);
            imagesRecycler.setItemAnimator(new DefaultItemAnimator());
            imagesRecycler.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            imagesRecycler.setAdapter(imagesAdaptar);
        }else{
            imagesRecycler.setAdapter(null);
        }

        //options
        optionViewModels = new ArrayList<>();
        optionsLinearLayout.setVisibility(View.VISIBLE);
        optionsLinearLayout.removeAllViews();
        int optionLoopnr = 0;
        for (Option option : poll.getOptions()) {
            OptionViewModel ovm = new OptionViewModel(optionsLinearLayout, this);
            ovm.setOption(option);
            if (poll.getChoiceIndex() != -1) {
                int hasVote = poll.getChoiceIndex() == optionLoopnr? 1 : 0;

                if(animate) {
                    ovm.option.setHasVote(hasVote);
                }else{
                    ovm.option.setHasVoteNoAnimation(hasVote);
                }
            }

            optionsLinearLayout.addView(ovm.getRoot());
            optionViewModels.add(ovm);
            optionLoopnr++;
        }

        updateMaximumVoteInOptions();

        startUpdating();

        bindingSetPoll(this.poll);
        binding.executePendingBindings();

        if(this.poll.poll.isNeedsUpdate()){
            reloadData();
        }
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
        ApiHelper.getApiService(binding.getRoot().getContext()).getPollById(poll.poll.get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PollResponse>() {
                    @Override
                    public void call(PollResponse pollResponse) {
                        Poll pollR = pollResponse.toPoll();
                        poll.poll.setNeedsUpdate(false);
                        poll.poll.setChoiceIndex(pollR.getChoiceIndex());
                        //update the votes
                        int optionLoopnr = 0;
                        for (Option optionItem : poll.poll.getOptions()) {
                            optionItem.setVotes(pollR.getOptions().get(optionLoopnr).getVotes());
                        }
                        updateMaximumVoteInOptions();

                        optionLoopnr = 0;
                        for (Option option : pollR.getOptions()) {
                            optionViewModels.get(optionLoopnr).option.getOption().setVotes(option.getVotes());
                            if (pollR.getChoiceIndex() != -1) {
                                int hasVote = pollR.getChoiceIndex() == optionLoopnr? 1 : 0;
                                optionViewModels.get(optionLoopnr).option.setHasVote(hasVote);
                            }

                            optionLoopnr++;
                        }

                    }
                });

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
        poll.poll.setChoiceIndex(index);
        updateMaximumVoteInOptions();

        PollsAccess.update(binding.getRoot().getContext(),
                new String[]{Contract.PollsDB.COLUMN_CHOICE_INDEX},
                new String[]{index+""}, Contract.PollsDB._ID, poll.poll.get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                });
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


    protected abstract void bindingSetPoll(PollBinderModel poll);

}
