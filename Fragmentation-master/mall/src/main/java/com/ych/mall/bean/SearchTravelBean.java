package com.ych.mall.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SearchTravelBean extends ParentBean {
    private List<SearchTravelData> data;

    public List<SearchTravelData> getData() {
        return data;
    }

    public void setData(List<SearchTravelData> data) {
        this.data = data;
    }

    public class SearchTravelData {
        private String pic_url;
        private String title;
        private String price_new;
        private String id;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
