package com.vandenbussche.emiel.projectsbp.binders.models;

import com.vandenbussche.emiel.projectsbp.models.Reaction;

/**
 * Created by emielPC on 11/11/16.
 */
public class ReactionBinderModel {
    public Reaction reaction;
    private float newNess = 1;

    public ReactionBinderModel(Reaction reaction) {
        this.reaction = reaction;
    }
}
