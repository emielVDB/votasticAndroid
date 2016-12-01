package com.vandenbussche.emiel.projectsbp.models;

/**
 * Created by emielPC on 1/12/16.
 */

public class Follow {
    private String pageId;
    private int flag;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public class Flags{
        public static final int UPLOAD_ADD = 1;
        public static final int UPLOAD_REMOVE = 2;
        public static final int OK = 3;

    }
}
