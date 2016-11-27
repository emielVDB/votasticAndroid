package com.vandenbussche.emiel.projectsbp.models;

import java.util.List;

/**
 * Created by emielPC on 26/11/16.
 */

public class Page {
    String _id;
    String title;
    List<String> tags;
    int pollsCount;
    int flag;

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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public static class Flags {
        public static final int NEW = 0;
        public static final int OK = 1;
        public static final int NEEDS_UPDATE = 2;
    }
}
