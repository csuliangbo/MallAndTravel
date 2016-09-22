package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/21.
 */
public class HomeMallBean extends ParentBean{
    public class Banner {

        private String banner_url;


        private String ad_link;


        public void setBanner_url(String banner_url) {

            this.banner_url = banner_url;

        }

        public String getBanner_url() {

            return this.banner_url;

        }

        public void setAd_link(String ad_link) {

            this.ad_link = ad_link;

        }

        public String getAd_link() {

            return this.ad_link;

        }

    }

    public class Center {

        private String center_url;


        private String ad_link;


        public void setCenter_url(String center_url) {

            this.center_url = center_url;

        }

        public String getCenter_url() {

            return this.center_url;

        }

        public void setAd_link(String ad_link) {

            this.ad_link = ad_link;

        }

        public String getAd_link() {

            return this.ad_link;

        }

    }

    public class Hot {

        private String hot_url;


        private String ad_link;


        public void setHot_url(String hot_url) {

            this.hot_url = hot_url;

        }

        public String getHot_url() {

            return this.hot_url;

        }

        public void setAd_link(String ad_link) {

            this.ad_link = ad_link;

        }

        public String getAd_link() {

            return this.ad_link;

        }

    }

    public class Class {

        private String class_url;


        private String class_id;


        private String class_name;


        public void setClass_url(String class_url) {

            this.class_url = class_url;

        }

        public String getClass_url() {

            return this.class_url;

        }

        public void setClass_id(String class_id) {

            this.class_id = class_id;

        }

        public String getClass_id() {

            return this.class_id;

        }

        public void setClass_name(String class_name) {

            this.class_name = class_name;

        }

        public String getClass_name() {

            return this.class_name;

        }

    }

    public class HomeMallData {

        private List<Banner> banner;


        private List<Center> center;


        private List<Hot> hot;


        private List<Class> clas;


        public void setBanner(List<Banner> banner) {

            this.banner = banner;

        }

        public List<Banner> getBanner() {

            return this.banner;

        }

        public void setCenter(List<Center> center) {

            this.center = center;

        }

        public List<Center> getCenter() {

            return this.center;

        }

        public void setHot(List<Hot> hot) {

            this.hot = hot;

        }

        public List<Hot> getHot() {

            return this.hot;

        }

        public void setClas(List<Class>clas) {

            this.clas=clas;

        }

        public List<Class> getClas() {

            return this.clas;

        }

    }

    private HomeMallData data;

    public HomeMallData getData() {
        return data;
    }

    public void setData(HomeMallData data) {
        this.data = data;
    }
}
