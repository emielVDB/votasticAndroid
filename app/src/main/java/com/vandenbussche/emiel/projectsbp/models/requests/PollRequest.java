package com.vandenbussche.emiel.projectsbp.models.requests;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.Reaction;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 18/11/16.
 */
public class PollRequest extends BaseObservable {
    private String question;
    private List<String> tags;
    private List<String> options;
    private String pageId;
    private int numberOfImages;

    public PollRequest(){}

    public PollRequest(Poll basePoll){
        this.question = basePoll.getQuestion();
        this.tags = basePoll.getTags();
        this.options = new ArrayList<>();
        for (Option option : basePoll.getOptions()) {
            this.options.add(option.getContent());
        }
        this.pageId = basePoll.getPageId();
        this.numberOfImages = basePoll.getNumberOfImages();
    }

    public Poll toPoll(){
        Poll poll = new Poll();
        poll.setQuestion(this.question);
        poll.setTags(this.tags);
        poll.set_id(((Long) ((Math.round(Math.random() * 10000000)))).toString());//random id maken(voorlopig)
        poll.setChoiceIndex(-1);
        poll.setReactions(new ArrayList<Reaction>());
        poll.setFlag(Poll.Flags.NEW);
        poll.setTotalReactions(0);
        poll.setTotalVotes(0);
        poll.setOptions(new ArrayList<Option>());
        for(String option : this.options){
            poll.getOptions().add(new Option(option));
        }
        poll.setPageId(this.pageId);
        //page name gets set manually!!!
        poll.setNumberOfImages(this.numberOfImages);
        return poll;
    }

    @Bindable
    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
        notifyPropertyChanged(BR.options);
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
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
        notifyPropertyChanged(BR.question);
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }
}
