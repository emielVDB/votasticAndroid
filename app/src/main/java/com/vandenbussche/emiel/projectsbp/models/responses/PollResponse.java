package com.vandenbussche.emiel.projectsbp.models.responses;

import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.Reaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 18/11/16.
 */
public class PollResponse {
    String _id;
    private String question;
    private List<String> tags;
    private List<Option> options;
    private int choiceIndex; // -1 = geen keuze
    private int totalVotes;
    private int totalReactions;
    private int flag;
    private long uploadTime;
    private String pageId;
    private String pageTitle;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getChoiceIndex() {
        return choiceIndex;
    }

    public void setChoiceIndex(int choiceIndex) {
        this.choiceIndex = choiceIndex;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int getTotalReactions() {
        return totalReactions;
    }

    public void setTotalReactions(int totalReactions) {
        this.totalReactions = totalReactions;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Poll toPoll(){
        Poll poll = new Poll();
        poll.set_id(get_id());
        poll.setQuestion(getQuestion());
        poll.setOptions(getOptions());
        poll.setTags(getTags());
        poll.setFlag(0);
        poll.setChoiceIndex(getChoiceIndex());
        poll.setPageId(getPageId());
        poll.setPageTitle(getPageTitle());
        poll.setReactions(new ArrayList<Reaction>());
        poll.setTotalReactions(getTotalReactions());
        poll.setTotalVotes(getTotalVotes());
        poll.setUploadTime(getUploadTime());

        return poll;
    }
}
