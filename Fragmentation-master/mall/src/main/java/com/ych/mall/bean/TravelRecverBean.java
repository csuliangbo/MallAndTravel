package com.ych.mall.bean;

/**
 * Created by Administrator on 2016/9/23.
 */
public class TravelRecverBean extends ParentBean {
    private TravelRecverData data;

    public TravelRecverData getData() {
        return data;
    }

    public void setData(TravelRecverData data) {
        this.data = data;
    }

    public class TravelRecverData{
        private Goods goods;
        private Address address;
        private JiFen jifen;

        public Goods getGoods() {
            return goods;
        }

        public void setGoods(Goods goods) {
            this.goods = goods;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public JiFen getJifen() {
            return jifen;
        }

        public void setJifen(JiFen jifen) {
            this.jifen = jifen;
        }
    }


    public class Goods {
        private String id;
        private String pic_url;
        private String title;
        private String price_new;
        private String chufa_address;
        private String fanli_jifen;
        private String is_baoyou;
        private String chufa_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice_new() {
            return price_new;
        }

        public void setPrice_new(String price_new) {
            this.price_new = price_new;
        }

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

        public String getIs_baoyou() {
            return is_baoyou;
        }

        public void setIs_baoyou(String is_baoyou) {
            this.is_baoyou = is_baoyou;
        }

        public String getChufa_date() {
            return chufa_date;
        }

        public void setChufa_date(String chufa_date) {
            this.chufa_date = chufa_date;
        }
    }

    public class Address {
        private String realname;
        private String mobile;
        private String prov;
        private String city;
        private String dist;
        private String address;
        private String zipcode;
        private String status;

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
    }

    public class JiFen {
        private String add_jf_limit;
        private String add_jf_currency;

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
