package com.ych.mall.event;

/**
 * Created by ych on 2016/8/31.
 */
public class MallAndTravelEvent {
    public static final int TYPE_CHANGE = -12;
    public static final int TYPE_GOODS = -13;
    public static final int TYPE_TRVEL = 14;
    private int currntType;
    public int position;

    public MallAndTravelEvent(int type, int position) {
        this.currntType = type;
        this.position = position;
    }

    public int getCurrntType() {
        return currntType;
    }

}
