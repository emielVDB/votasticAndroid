package com.vandenbussche.emiel.projectsbp.models;

/**
 * Created by emielPC on 11/11/16.
 */
public class Reaction {
    String text;
    Long time;

    public Reaction(){}
    public Reaction(String text, Long time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
