package com.vandenbussche.emiel.projectsbp.models.responses;

/**
 * Created by emielPC on 9/12/16.
 */

public class PollUpdateResponse {
    String kind;
    VoteResponse  voteResponse;

    public VoteResponse getVoteResponse() {
        return voteResponse;
    }

    public void setVoteResponse(VoteResponse voteResponse) {
        this.voteResponse = voteResponse;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
