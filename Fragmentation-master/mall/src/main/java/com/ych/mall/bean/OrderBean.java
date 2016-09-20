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

        private String orders_num;


        private String price_sum;


        private String create_time;


        private String orders_status;


        private String order_type;


        private String goods_id;


        private String goods_title;


        private String goods_num;


        private String price_new;


        private String pic_url;


        private String chufa_date;


        private String chufa_price;


        private String goods_orders_id;


        public void setOrders_num(String orders_num){

            this.orders_num = orders_num;

        }

        public String getOrders_num(){

            return this.orders_num;

        }

        public void setPrice_sum(String price_sum){

            this.price_sum = price_sum;

        }

        public String getPrice_sum(){

            return this.price_sum;

        }

        public void setCreate_time(String create_time){

            this.create_time = create_time;

        }

        public String getCreate_time(){

            return this.create_time;

        }

        public void setOrders_status(String orders_status){

            this.orders_status = orders_status;

        }

        public String getOrders_status(){

            return this.orders_status;

        }

        public void setOrder_type(String order_type){

            this.order_type = order_type;

        }

        public String getOrder_type(){

            return this.order_type;

        }

        public void setGoods_id(String goods_id){

            this.goods_id = goods_id;

        }

        public String getGoods_id(){

            return this.goods_id;

        }

        public void setGoods_title(String goods_title){

            this.goods_title = goods_title;

        }

        public String getGoods_title(){

            return this.goods_title;

        }

        public void setGoods_num(String goods_num){

            this.goods_num = goods_num;

        }

        public String getGoods_num(){

            return this.goods_num;

        }

        public void setPrice_new(String price_new){

            this.price_new = price_new;

        }

        public String getPrice_new(){

            return this.price_new;

        }

        public void setPic_url(String pic_url){

            this.pic_url = pic_url;

        }

        public String getPic_url(){

            return this.pic_url;

        }

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

        public void setGoods_orders_id(String goods_orders_id){

            this.goods_orders_id = goods_orders_id;

        }

        public String getGoods_orders_id(){

            return this.goods_orders_id;

        }

    }
}
