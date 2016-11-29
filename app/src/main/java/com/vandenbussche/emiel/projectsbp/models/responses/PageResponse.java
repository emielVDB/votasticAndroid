package com.vandenbussche.emiel.projectsbp.models.responses;

import com.vandenbussche.emiel.projectsbp.models.Option;

import java.util.List;

/**
 * Created by emielPC on 18/11/16.
 */
public class PageResponse {
    String _id;
    private String title;
    private List<String> tags;
    private int pollsCount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getPollsCount() {
        return pollsCount;
    }

    public void setPollsCount(int pollsCount) {
        this.pollsCount = pollsCount;
    }
}
