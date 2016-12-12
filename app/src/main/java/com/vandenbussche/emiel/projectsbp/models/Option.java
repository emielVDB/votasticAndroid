package com.vandenbussche.emiel.projectsbp.models;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by emielPC on 10/11/16.
 */
public class Option extends BaseObservable{
    private int votes;
    private String content;

    public Option(String content){
        votes = 0;
        this.content = content;
    }
    public Option(int votes, String content) {
        this.votes = votes;
        this.content = content;
    }

    @Bindable
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
        notifyPropertyChanged(BR.votes);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
