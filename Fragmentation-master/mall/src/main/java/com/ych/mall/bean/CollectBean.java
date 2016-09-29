package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/14.
 */
public class CollectBean extends ParentBean{
    private List<CollectData> data;

    public List<CollectData> getData() {
        return data;
    }

    public void setData(List<CollectData> data) {
        this.data = data;
    }

    public class CollectData {

        private String id;
        private String goods_id;


        private String pic_url;


        private String price_new;


        private String detail_title;


        private String is_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setGoods_id(String goods_id){

            this.goods_id = goods_id;

        }

        public String getGoods_id(){

            return this.goods_id;

        }

        public void setPic_url(String pic_url){

            this.pic_url = pic_url;

        }

        public String getPic_url(){

            return this.pic_url;

        }

        public void setPrice_new(String price_new){

            this.price_new = price_new;

        }

        public String getPrice_new(){

            return this.price_new;

        }

        public void setDetail_title(String detail_title){

            this.detail_title = detail_title;

        }

        public String getDetail_title(){

            return this.detail_title;

        }

        public void setIs_type(String is_type){

            this.is_type = is_type;

        }

        public String getIs_type(){

            return this.is_type;

        }

    }
}
