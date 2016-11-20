package com.vandenbussche.emiel.projectsbp.binders.models;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.models.Option;

/**
 * Created by emielPC on 11/11/16.
 */
public class OptionBinderModel extends BaseObservable{
    Option option;

    int hasVote = -1; //-1 = not decided, 0 = not chosen, 1 = chosen
    float showPercentage = 0;
    float newNess = 0;
    int maxVotes = 1;

    public OptionBinderModel(Option option) {
        this.option = option;
    }

    @Bindable
    public int getMaxVotes() {
        return maxVotes;
    }

    public void setMaxVotes(int maxVotes) {
        this.maxVotes = maxVotes;
        notifyPropertyChanged(BR.maxVotes);
    }

    @Bindable
    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
        notifyPropertyChanged(BR.option);
    }

    @Bindable
    public int getHasVote() {
        return hasVote;
    }

    public void setHasVote(int hasVote) {
        this.hasVote = hasVote;
        notifyPropertyChanged(BR.hasVote);
        setShowPercentage(0);
        new Thread(run).start();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            while(getShowPercentage() < 99.9f) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setShowPercentage(getShowPercentage() + (100f - getShowPercentage()) / 30f);
            }
            setShowPercentage(100);
        }
    };


    @Bindable
    public float getNewNess() {
        return newNess;
    }

    public void setNewNess(float newNess) {
        this.newNess = newNess;
    }


    @Bindable
    public float getShowPercentage() {
        return showPercentage;
    }

    public void setShowPercentage(float showPercentage) {
        this.showPercentage = showPercentage;
        notifyPropertyChanged(BR.showPercentage);
    }
}
