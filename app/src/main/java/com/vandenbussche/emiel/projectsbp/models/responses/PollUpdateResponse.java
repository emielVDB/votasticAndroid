package com.vandenbussche.emiel.projectsbp.models.responses;

/**
 * Created by emielPC on 9/12/16.
 */

public class PollUpdateResponse {
    String kind;
    VoteResponse  voteResponse;
    AddReactionResponse addReactionResponse;

    public VoteResponse getVoteResponse() {
        return voteResponse;
    }

    public void setVoteResponse(VoteResponse voteResponse) {
        this.voteResponse = voteResponse;
    }

    public AddReactionResponse getAddReactionResponse() {
        return addReactionResponse;
    }

    public void setAddReactionResponse(AddReactionResponse addReactionResponse) {
        this.addReactionResponse = addReactionResponse;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }


}
