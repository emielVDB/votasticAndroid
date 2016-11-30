package com.vandenbussche.emiel.projectsbp.models.requests;

/**
 * Created by emielPC on 30/11/16.
 */

public class SearchRequest {
    String text;
    long maxUploadTime;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMaxUploadTime() {
        return maxUploadTime;
    }

    public void setMaxUploadTime(long maxUploadTime) {
        this.maxUploadTime = maxUploadTime;
    }
}
