package com.vandenbussche.emiel.projectsbp.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

/**
 * Created by emielPC on 11/11/16.
 */
public class PollList extends BaseObservable {
    private List<Poll> data;

    @Bindable
    public List<Poll> getData() {
        return data;
    }

    public void setData(List<Poll> data) {
        this.data = data;
    }
}
