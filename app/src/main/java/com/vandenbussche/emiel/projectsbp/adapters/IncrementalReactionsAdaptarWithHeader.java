package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vandenbussche.emiel.projectsbp.models.Reaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 30/11/16.
 */

public class IncrementalReactionsAdaptarWithHeader extends ReactionsAdaptarWithHeader {
    NewReactionsNeededListener newReactionsNeededListener;
    private int maxPos = -1;
    public IncrementalReactionsAdaptarWithHeader(List<Reaction> reactionList, Context context, ReactionsAdapterWithHeaderListener listener, NewReactionsNeededListener newReactionsNeededListener, int headerLayout) {
        super(reactionList, context, listener, headerLayout);
        this.newReactionsNeededListener = newReactionsNeededListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(newReactionsNeededListener != null) {
            if (position >= this.reactionList.size() - 1 && maxPos < position) {
                maxPos = position;
                if(reactionList != null && reactionList.size() > 0) {
                    newReactionsNeededListener.getNewReactions(reactionList.get(reactionList.size() - 1));
                }else{
                    newReactionsNeededListener.getNewReactions(null);
                }
            }
        }
    }

    public void addReactions(List<Reaction> reactions){
        int insertedIndex = this.reactionList.size();
        this.reactionList.addAll(reactions);
        notifyItemRangeInserted(insertedIndex + 1, reactions.size());
    }

    public void addNewReaction(Reaction reaction){
        this.reactionList.add(0, reaction);
        notifyItemInserted(1);
    }

    public void clearItems() {
        maxPos = 0;
        reactionList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface NewReactionsNeededListener {
        public void getNewReactions(Reaction lastReaction);
    }


}
