package com.ych.mall.event;

/**
 * Created by Administrator on 2016/10/20.
 */
public class RefundEvent {
    public RefundEvent(Boolean isRefund) {
        this.isRefund = isRefund;
    }

    private boolean isRefund;

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }

}
