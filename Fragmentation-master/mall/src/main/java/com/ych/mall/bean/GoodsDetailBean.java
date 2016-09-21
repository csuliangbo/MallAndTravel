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


        private List<String> pic_tuji;


        private String kucun;


        private String fanli_jifen;


        private List<Taocan> taocan;


        private String price_old;


        private String price_new;


        private String is_baoyou;


        private String pingjia_content;


        private String goods_id;


        private String pingjia_time;


        private String username;


        private String shuoming;


        public void setId(String id){

            this.id = id;

        }

        public String getId(){

            return this.id;

        }

        public void setTitle(String title){

            this.title = title;

        }

        public String getTitle(){

            return this.title;

        }

        public void setPic_url(String pic_url){

            this.pic_url = pic_url;

        }

        public String getPic_url(){

            return this.pic_url;

        }

        public List<String> getPic_tuji() {
            return pic_tuji;
        }

        public void setPic_tuji(List<String> pic_tuji) {
            this.pic_tuji = pic_tuji;
        }

        public void setKucun(String kucun){

            this.kucun = kucun;

        }

        public String getKucun(){

            return this.kucun;

        }

        public void setFanli_jifen(String fanli_jifen){

            this.fanli_jifen = fanli_jifen;

        }

        public String getFanli_jifen(){

            return this.fanli_jifen;

        }

        public List<Taocan> getTaocan() {
            return taocan;
        }

        public void setTaocan(List<Taocan> taocan) {
            this.taocan = taocan;
        }

        public void setPrice_old(String price_old){

            this.price_old = price_old;

        }

        public String getPrice_old(){

            return this.price_old;

        }

        public void setPrice_new(String price_new){

            this.price_new = price_new;

        }

        public String getPrice_new(){

            return this.price_new;

        }

        public void setIs_baoyou(String is_baoyou){

            this.is_baoyou = is_baoyou;

        }

        public String getIs_baoyou(){

            return this.is_baoyou;

        }

        public void setPingjia_content(String pingjia_content){

            this.pingjia_content = pingjia_content;

        }

        public String getPingjia_content(){

            return this.pingjia_content;

        }

        public void setGoods_id(String goods_id){

            this.goods_id = goods_id;

        }

        public String getGoods_id(){

            return this.goods_id;

        }

        public void setPingjia_time(String pingjia_time){

            this.pingjia_time = pingjia_time;

        }

        public String getPingjia_time(){

            return this.pingjia_time;

        }

        public void setUsername(String username){

            this.username = username;

        }

        public String getUsername(){

            return this.username;

        }

        public void setShuoming(String shuoming){

            this.shuoming = shuoming;

        }

        public String getShuoming(){

            return this.shuoming;

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
