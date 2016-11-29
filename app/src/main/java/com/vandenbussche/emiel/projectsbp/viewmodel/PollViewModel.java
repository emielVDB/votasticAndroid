package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.binders.models.PollBinderModel;
import com.vandenbussche.emiel.projectsbp.databinding.RowPollBinding;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 11/11/16.
 */
public class PollViewModel {
    RowPollBinding binding;
    PollBinderModel poll;

    LinearLayout optionsLinearLayout;

    List<OptionViewModel> optionViewModels;


    public PollViewModel(ViewGroup parent){
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_poll, parent, false);
        optionsLinearLayout = (LinearLayout)binding.getRoot().findViewById(R.id.optionsLinearLayout);
    }

    public void setPoll(Poll poll){
        this.poll = new PollBinderModel(poll);
        binding.setPoll(this.poll);
        binding.executePendingBindings();

        optionViewModels = new ArrayList<>();

        optionsLinearLayout.removeAllViews();
        for (Option option : poll.getOptions()) {
            OptionViewModel ovm = new OptionViewModel(optionsLinearLayout, this);
            ovm.setOption(option);
            optionsLinearLayout.addView(ovm.getRoot());
            optionViewModels.add(ovm);

        }

        updateMaximumVoteInOptions();
    }


    public View getRoot(){
        return binding.getRoot();
    }

    public void startUpdating(){}

    public void stopUpdating(){}

    public void reloadData(){

    }

    public void optionClicked(Option option){
        //alle andere op 0zetten
        for (OptionViewModel optionVM :
                optionViewModels) {

            optionVM.option.setHasVote(optionVM.option.getOption() == option? 1 : 0);
            optionVM.option.setNewNess(1);
        }
    }

    public void updateMaximumVoteInOptions(){
        int maxVotes = 0;
        for(Option option : poll.poll.getOptions()){
            if(option.getVotes() > maxVotes){
                maxVotes = option.getVotes();
            }
        }

        for(OptionViewModel ovm : optionViewModels){
            ovm.option.setMaxVotes(maxVotes);
        }
    }
}
