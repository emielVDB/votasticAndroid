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
    String question;
    List<String> tags;
    List<String> options;

    public PollRequest(){}

    public PollRequest(Poll basePoll){
        this.question = basePoll.getQuestion();
        this.tags = basePoll.getTags();
        this.options = new ArrayList<>();
        for (Option option : basePoll.getOptions()) {
            this.options.add(option.getContent());
        }
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
}
