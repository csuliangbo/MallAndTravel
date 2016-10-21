package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/14.
 */
public class OrderBean extends ParentBean {
    private List<OrderData> data;

    public List<OrderData> getData() {
        return data;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }

    public class OrderData {
        private String id;

        private String orders_num;

        private String price_shiji;

        private String create_time;

        private String orders_status;

        private String order_type;
        private String pay_status;

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        private List<Goods> goods;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrders_num() {
            return orders_num;
        }

        public void setOrders_num(String orders_num) {
            this.orders_num = orders_num;
        }

        public String getPrice_shiji() {
            return price_shiji;
        }

        public void setPrice_shiji(String price_shiji) {
            this.price_shiji = price_shiji;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getOrders_status() {
            return orders_status;
        }

        public void setOrders_status(String orders_status) {
            this.orders_status = orders_status;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public List<Goods> getGoods() {
            return goods;
        }

        public void setGoods(List<Goods> goods) {
            this.goods = goods;
        }


    }

    public class Goods {
        private String id;

        private String goods_id;

        private String goods_title;

        private String goods_num;

        private String price_new;

        private String pic_url;

        private String chufa_date;

        private String chufa_price;

        private String goods_orders_id;

        private String chufa_price_et;

        private String goods_num_et;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_title() {
            return goods_title;
        }

        public void setGoods_title(String goods_title) {
            this.goods_title = goods_title;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getPrice_new() {
            return price_new;
        }

        public void setPrice_new(String price_new) {
            this.price_new = price_new;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getChufa_date() {
            return chufa_date;
        }

        public void setChufa_date(String chufa_date) {
            this.chufa_date = chufa_date;
        }

        public String getChufa_price() {
            return chufa_price;
        }

        public void setChufa_price(String chufa_price) {
            this.chufa_price = chufa_price;
        }

        public String getGoods_orders_id() {
            return goods_orders_id;
        }

        public void setGoods_orders_id(String goods_orders_id) {
            this.goods_orders_id = goods_orders_id;
        }

        public String getChufa_price_et() {
            return chufa_price_et;
        }

        public void setChufa_price_et(String chufa_price_et) {
            this.chufa_price_et = chufa_price_et;
        }

        public String getGoods_num_et() {
            return goods_num_et;
        }

        public void setGoods_num_et(String goods_num_et) {
            this.goods_num_et = goods_num_et;
        }
    }
}
