package com.vandenbussche.emiel.projectsbp.models.requests;

/**
 * Created by emielPC on 9/12/16.
 */

public class ReactionRequest {
    private String pollId;
    private String content;

    public ReactionRequest(String pollId, String content) {
        this.pollId = pollId;
        this.content = content;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
