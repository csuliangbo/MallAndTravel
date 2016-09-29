package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/21.
 */
public class TravelDetailBean extends ParentBean {

    private List<TravelDetailData> data;

    public List<TravelDetailData> getData() {
        return data;
    }

    public void setData(List<TravelDetailData> data) {
        this.data = data;
    }
    public class Taocan {

        private String chufa_date;


        private String chufa_price;


        private String chufa_price_et;


        public void setChufa_date(String chufa_date){

            this.chufa_date = chufa_date;

        }

        public String getChufa_date(){

            return this.chufa_date;

        }

        public void setChufa_price(String chufa_price){

            this.chufa_price = chufa_price;

        }

        public String getChufa_price(){

            return this.chufa_price;

        }

        public void setChufa_price_et(String chufa_price_et){

            this.chufa_price_et = chufa_price_et;

        }

        public String getChufa_price_et(){

            return this.chufa_price_et;

        }

    }
    public class TravelDetailData {

        private String id;

        private String title;

        private String pic_url;

        private List<String> pic_tuji ;

        private List<Taocan> taocan;

        private String price_old;

        private String price_new;

        private String chufa_address;

        private String fanli_jifen;

        private String shoper_id;

        private String shangjia;

        public String getChufa_address() {
            return chufa_address;
        }

        public void setChufa_address(String chufa_address) {
            this.chufa_address = chufa_address;
        }

        public String getFanli_jifen() {
            return fanli_jifen;
        }

        public void setFanli_jifen(String fanli_jifen) {
            this.fanli_jifen = fanli_jifen;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getPic_tuji() {
            return pic_tuji;
        }

        public void setPic_tuji(List<String> pic_tuji) {
            this.pic_tuji = pic_tuji;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getPrice_new() {
            return price_new;
        }

        public void setPrice_new(String price_new) {
            this.price_new = price_new;
        }

        public String getPrice_old() {
            return price_old;
        }

        public void setPrice_old(String price_old) {
            this.price_old = price_old;
        }

        public String getShangjia() {
            return shangjia;
        }

        public void setShangjia(String shangjia) {
            this.shangjia = shangjia;
        }

        public String getShoper_id() {
            return shoper_id;
        }

        public void setShoper_id(String shoper_id) {
            this.shoper_id = shoper_id;
        }

        public List<Taocan> getTaocan() {
            return taocan;
        }

        public void setTaocan(List<Taocan> taocan) {
            this.taocan = taocan;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
