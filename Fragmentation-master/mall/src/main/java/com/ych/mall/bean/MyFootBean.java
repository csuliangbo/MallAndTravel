package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/14.
 */
public class MyFootBean extends ParentBean {
private List<MyFootData> data;

    public List<MyFootData> getData() {
        return data;
    }

    public void setData(List<MyFootData> data) {
        this.data = data;
    }

    public class MyFootData {


        private String gid;


        private String goods_pic;


        private String goods_name;


        private String goods_price;


        private String is_type;


        public void setGid(String gid) {

            this.gid = gid;

        }

        public String getGid() {

            return this.gid;

        }

        public void setGoods_pic(String goods_pic) {

            this.goods_pic = goods_pic;

        }

        public String getGoods_pic() {

            return this.goods_pic;

        }

        public void setGoods_name(String goods_name) {

            this.goods_name = goods_name;

        }

        public String getGoods_name() {

            return this.goods_name;

        }

        public void setGoods_price(String goods_price) {

            this.goods_price = goods_price;

        }

        public String getGoods_price() {

            return this.goods_price;

        }

        public void setIs_type(String is_type) {

            this.is_type = is_type;

        }

        public String getIs_type() {

            return this.is_type;

        }

    }

}
