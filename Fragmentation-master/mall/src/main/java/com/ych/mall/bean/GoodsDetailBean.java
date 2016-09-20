package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/19.
 */
public class GoodsDetailBean extends ParentBean {
    public class Taocan {

        private String guige_title;


        private String guige_price_new;


        private String guige_zhongliang;


        public void setGuige_title(String guige_title){

            this.guige_title = guige_title;

        }

        public String getGuige_title(){

            return this.guige_title;

        }

        public void setGuige_price_new(String guige_price_new){

            this.guige_price_new = guige_price_new;

        }

        public String getGuige_price_new(){

            return this.guige_price_new;

        }

        public void setGuige_zhongliang(String guige_zhongliang){

            this.guige_zhongliang = guige_zhongliang;

        }

        public String getGuige_zhongliang(){

            return this.guige_zhongliang;

        }

    }
    public class GoodsDetailData {

        private String id;


        private String title;


        private String pic_url;


        private List<String> pic_tuji ;


        private String kucun;


        private String fanli_jifen;


        private List<Taocan> taocan;


        private List<String> shuoming ;


        private String price_old;


        private String price_new;


        private String is_baoyou;


        private String pingjia_content;


        private String goods_id;


        private String pingjia_time;


        private String username;

        public List<Taocan> getTaocan() {
            return taocan;
        }

        public void setTaocan(List<Taocan> taocan) {
            this.taocan = taocan;
        }

        public String getFanli_jifen() {
            return fanli_jifen;
        }

        public void setFanli_jifen(String fanli_jifen) {
            this.fanli_jifen = fanli_jifen;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_baoyou() {
            return is_baoyou;
        }

        public void setIs_baoyou(String is_baoyou) {
            this.is_baoyou = is_baoyou;
        }

        public String getKucun() {
            return kucun;
        }

        public void setKucun(String kucun) {
            this.kucun = kucun;
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

        public String getPingjia_content() {
            return pingjia_content;
        }

        public void setPingjia_content(String pingjia_content) {
            this.pingjia_content = pingjia_content;
        }

        public String getPingjia_time() {
            return pingjia_time;
        }

        public void setPingjia_time(String pingjia_time) {
            this.pingjia_time = pingjia_time;
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

        public List<String> getShuoming() {
            return shuoming;
        }

        public void setShuoming(List<String> shuoming) {
            this.shuoming = shuoming;
        }



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private List<GoodsDetailData> data;

    public List<GoodsDetailData> getData() {
        return data;
    }

    public void setData(List<GoodsDetailData> data) {
        this.data = data;
    }
}
