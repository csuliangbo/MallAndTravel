package com.ych.mall.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class PayBean extends ParentBean {
    private List<PayData> data;

    public List<PayData> getData() {
        return data;
    }

    public void setData(List<PayData> data) {
        this.data = data;
    }

    public class PayData {
        private String id;
        private String cart_id;
        private String title;
        private String goods_num;
        private String pic_url;
        private String price_new;
        private String fanli_jifen;
        private String taocan_name;
        private String kucun;
        private String is_baoyou;
        private String realname;
        private String mobile;
        private String prov;
        private String city;
        private String dist;
        private String address;
        private String zipcode;
        private String status;
        private String add_jf_limit;
        private String add_jf_currency;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
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

        public String getFanli_jifen() {
            return fanli_jifen;
        }

        public void setFanli_jifen(String fanli_jifen) {
            this.fanli_jifen = fanli_jifen;
        }

        public String getTaocan_name() {
            return taocan_name;
        }

        public void setTaocan_name(String taocan_name) {
            this.taocan_name = taocan_name;
        }

        public String getKucun() {
            return kucun;
        }

        public void setKucun(String kucun) {
            this.kucun = kucun;
        }

        public String getIs_baoyou() {
            return is_baoyou;
        }

        public void setIs_baoyou(String is_baoyou) {
            this.is_baoyou = is_baoyou;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProv() {
            return prov;
        }

        public void setProv(String prov) {
            this.prov = prov;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDist() {
            return dist;
        }

        public void setDist(String dist) {
            this.dist = dist;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAdd_jf_limit() {
            return add_jf_limit;
        }

        public void setAdd_jf_limit(String add_jf_limit) {
            this.add_jf_limit = add_jf_limit;
        }

        public String getAdd_jf_currency() {
            return add_jf_currency;
        }

        public void setAdd_jf_currency(String add_jf_currency) {
            this.add_jf_currency = add_jf_currency;
        }
    }
}
