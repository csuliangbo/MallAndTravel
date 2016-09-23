package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/21.
 */
public class TravelDetailBean extends ParentBean {

private List<TravelDetaiData> data;

    public List<TravelDetaiData> getData() {
        return data;
    }

    public void setData(List<TravelDetaiData> data) {
        this.data = data;
    }



    public class TravelDetaiData {
        @Override
        public String toString() {
            return "TravelDetaiData{" +
                    "chufa_address='" + chufa_address + '\'' +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", pic_url='" + pic_url + '\'' +
                    ", pic_tuji=" + pic_tuji +
                    ", price_old='" + price_old + '\'' +
                    ", price_new='" + price_new + '\'' +
                    ", chufa_date='" + chufa_date + '\'' +
                    ", fanli_jifen='" + fanli_jifen + '\'' +
                    '}';
        }

        private String id;


        private String title;


        private String pic_url;


        private List<String> pic_tuji ;


        private String price_old;


        private String price_new;


        private String chufa_address;


        private List<String> chufa_date;


        private String fanli_jifen;


        public List<String> getChufa_date() {
            return chufa_date;
        }

        public void setChufa_date(List<String> chufa_date) {
            this.chufa_date = chufa_date;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
