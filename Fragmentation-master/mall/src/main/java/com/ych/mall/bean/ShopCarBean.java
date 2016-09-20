package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/8.
 */
public class ShopCarBean extends ParentBean {
    private List<ShopCarData> data;

    public List<ShopCarData> getData() {
        return data;
    }

    public void setData(List<ShopCarData> data) {
        this.data = data;
    }

    public class ShopCarData {

        private String cart_id;


        private String id;


        private String title;


        private String pic_url;


        private String price_new;


        private String fanli_jifen;


        private String is_baoyou;


        private String goods_num;


        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public void setCart_id(String cart_id) {

            this.cart_id = cart_id;

        }

        public String getCart_id() {

            return this.cart_id;

        }

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

        public void setIs_baoyou(String is_baoyou) {

            this.is_baoyou = is_baoyou;

        }

        public String getIs_baoyou() {

            return this.is_baoyou;

        }

        public void setGoods_num(String goods_num) {

            this.goods_num = goods_num;

        }

        public String getGoods_num() {

            return this.goods_num;

        }

    }
}
