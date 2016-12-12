package com.vandenbussche.emiel.projectsbp.models.requests;

/**
 * Created by emielPC on 9/12/16.
 */

public class VoteRequest {
    private String pollId;
    private int voteIndex;

    public VoteRequest(String poll_id, int voteIndex) {
        this.pollId = poll_id;
        this.voteIndex = voteIndex;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public int getVoteIndex() {
        return voteIndex;
    }

    public void setVoteIndex(int voteIndex) {
        this.voteIndex = voteIndex;
    }
}
