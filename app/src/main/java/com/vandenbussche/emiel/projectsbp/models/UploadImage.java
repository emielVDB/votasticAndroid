package com.vandenbussche.emiel.projectsbp.models;

import java.util.List;

/**
 * Created by emielPC on 20/01/17.
 */

public class UploadImage {
    int _id;
    String pollId;
    int index;
    String url;
    int flag;

    public static class Flags {
        public static final int NEW = 0;
        public static final int READY_TO_UPLOAD = 1;
        public static final int UPLOADING = 2;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
