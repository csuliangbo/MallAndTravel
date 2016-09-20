package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/5.
 */
public class GoodsListBean extends ParentBean {

    private List<GoodsListData> data;

    public List<GoodsListData> getData() {
        return data;
    }

    public void setData(List<GoodsListData> data) {
        this.data = data;
    }

    public class GoodsListData {

        private String id;


        private String title;


        private String pic_url;


        private String price_old;


        private String price_new;


        private String fanli_jifen;


        public void setId(String id) {

            this.id = id;

        }

        public String getId() {

            return this.id;

        }

        public void setTitle(String title) {

            this.title = title;

        }

        public String getTitle() {

            return this.title;

        }

        public void setPic_url(String pic_url) {

            this.pic_url = pic_url;

        }

        public String getPic_url() {

            return this.pic_url;

        }

        public void setPrice_old(String price_old) {

            this.price_old = price_old;

        }

        public String getPrice_old() {

            return this.price_old;

        }

        public void setPrice_new(String price_new) {

            this.price_new = price_new;

        }

        public String getPrice_new() {

            return this.price_new;

        }

        public void setFanli_jifen(String fanli_jifen) {

            this.fanli_jifen = fanli_jifen;

        }

        public String getFanli_jifen() {

            return this.fanli_jifen;

        }

    }
}
