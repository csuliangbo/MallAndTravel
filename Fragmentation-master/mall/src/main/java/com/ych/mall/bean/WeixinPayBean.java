package com.ych.mall.bean;

/**
 * Created by Administrator on 2016/10/13.
 */
public class WeixinPayBean extends ParentBean {
    private WeixinPayData data;

    public WeixinPayData getData() {
        return data;
    }

    public void setData(WeixinPayData data) {
        this.data = data;
    }

    public class WeixinPayData {
        private String appid;
        private String sign;
        private String noncestr;
        private String partnerid;
        private String prepayid;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
