package com.vandenbussche.emiel.projectsbp.models;

/**
 * Created by emielPC on 11/11/16.
 */
public class Reaction {
    String content;
    long uploadTime;

    public Reaction(){}
    public Reaction(String content, Long time) {
        this.content = content;
        this.uploadTime = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }
}
