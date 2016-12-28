package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.binders.models.OptionBinderModel;
import com.vandenbussche.emiel.projectsbp.binders.models.PollBinderModel;
import com.vandenbussche.emiel.projectsbp.databinding.RowOptionBinding;
import com.vandenbussche.emiel.projectsbp.databinding.RowPollBinding;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollBindable;

/**
 * Created by emielPC on 11/11/16.
 */
public class OptionViewModel implements View.OnClickListener{
    PollBindable pollViewModel;

    RowOptionBinding binding;
    public OptionBinderModel option;


    public OptionViewModel(ViewGroup parent, PollBindable pollViewModel){
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_option, parent, false);
        this.pollViewModel = pollViewModel;
    }

    public void setOption(Option option){
        this.option = new OptionBinderModel(option);
        binding.setOption(this.option);
        binding.executePendingBindings();
        binding.btnChose.setOnClickListener(this);
    }

    public View getRoot(){
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        pollViewModel.optionClicked(option.getOption());
    }
}
