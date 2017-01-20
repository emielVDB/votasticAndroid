package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vandenbussche.emiel.projectsbp.models.Poll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 30/11/16.
 */

public class IncrementalPollsAdaptarWithHeader extends PollsAdaptarWithHeader {
    NewPollsNeededListener newPollsNeededListener;
    private int maxPos = 0;
    public IncrementalPollsAdaptarWithHeader(List<Poll> pollList, Context context, PollsAdapterWithHeaderListener listener, NewPollsNeededListener newPollsNeededListener, int headerLayout) {
        super(pollList, context, listener, headerLayout);
        this.newPollsNeededListener = newPollsNeededListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(newPollsNeededListener != null) {
            if (position >= this.pollList.size() - 1 && maxPos < position) {
                maxPos = position;
                newPollsNeededListener.getNewPolls(pollList.get(pollList.size() - 1));
            }
        }
    }

    public void addPolls(List<Poll> polls){
        int insertedIndex = this.pollList.size();
        this.pollList.addAll(polls);
        notifyItemRangeInserted(insertedIndex + 1 , polls.size());
    }

    public void clearItems() {
        maxPos = 0;
        pollList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface NewPollsNeededListener {
        public void getNewPolls(Poll lastPoll);
    }


}
