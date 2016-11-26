package com.vandenbussche.emiel.projectsbp.models.requests;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.Reaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 18/11/16.
 */
public class PageRequest extends BaseObservable {
    String title;
    List<String> tags;

    public PageRequest(){}

    public PageRequest(Page basePoll){
        this.title = basePoll.getTitle();
        this.tags = basePoll.getTags();

    }

    public Page toPage(){
        Page page = new Page();
        page.setTitle(this.title);
        page.setTags(this.tags);
        page.set_id(((Long) ((Math.round(Math.random() * 10000000)))).toString());//random id maken(voorlopig)
        page.setPollsCount(0);
        return page;
    }
    

    @Bindable
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        notifyPropertyChanged(BR.tags);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }
}
