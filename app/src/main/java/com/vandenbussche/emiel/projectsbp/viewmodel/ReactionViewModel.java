package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.RowReactionBinding;
import com.vandenbussche.emiel.projectsbp.models.Reaction;

/**
 * Created by emielPC on 11/11/16.
 */
public class ReactionViewModel {
    private RowReactionBinding binding;
    private Reaction reaction;

    public ReactionViewModel(ViewGroup parent){
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_reaction, parent, false);
    }

    public void setReaction(final Reaction reaction){
        this.reaction = reaction;
        binding.setReaction(this.reaction);
        binding.executePendingBindings();
    }

    public View getRoot(){
        return binding.getRoot();
    }
}
