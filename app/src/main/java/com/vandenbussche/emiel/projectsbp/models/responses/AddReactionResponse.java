package com.vandenbussche.emiel.projectsbp.models.responses;

import com.vandenbussche.emiel.projectsbp.models.Reaction;

/**
 * Created by emielPC on 28/12/16.
 */

public class AddReactionResponse {
    String content;
    long uploadTime;

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

    public Reaction toReaction(){
        return new Reaction(content, uploadTime);
    }
}
