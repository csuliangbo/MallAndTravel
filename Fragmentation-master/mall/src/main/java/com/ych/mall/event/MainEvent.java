package com.ych.mall.event;

/**
 * Created by ych on 2016/9/18.
 */
public class MainEvent {
    int position;

    boolean bottomStatus=true;
    public MainEvent(int p) {
        this.position = p;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {

        this.position = position;
    }

    public boolean isBottomStatus() {
        return bottomStatus;
    }

    public void setBottomStatus(boolean bottomStatus) {
        this.bottomStatus = bottomStatus;
    }
}
