package com.ych.mall.event;

/**
 * Created by Administrator on 2016/10/18.
 */
public class WeixinPayEvent {
    public WeixinPayEvent(String errcode, String errStr) {
        this.errcode = errcode;
        this.errStr = errStr;
    }

    private String errcode;
    private String errStr;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }
}
