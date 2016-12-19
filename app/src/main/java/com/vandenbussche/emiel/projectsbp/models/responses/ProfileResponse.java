package com.vandenbussche.emiel.projectsbp.models.responses;

import java.util.List;

/**
 * Created by emielPC on 16/12/16.
 */

public class ProfileResponse {
    private long birthDay;
    private int gender;
    private List<String> followingPages;

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public List<String> getFollowingPages() {
        return followingPages;
    }

    public void setFollowingPages(List<String> followingPages) {
        this.followingPages = followingPages;
    }
}
