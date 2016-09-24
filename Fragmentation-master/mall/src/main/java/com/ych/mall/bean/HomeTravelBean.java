package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/22.
 */
public class HomeTravelBean extends ParentBean{
    public class Bannner {

        private String bannner_url;


        private String ad_link;


        public void setBannner_url(String bannner_url){

            this.bannner_url = bannner_url;

        }

        public String getBannner_url(){

            return this.bannner_url;

        }

        public void setAd_link(String ad_link){

            this.ad_link = ad_link;

        }

        public String getAd_link(){

            return this.ad_link;

        }

    }
    public class Class_page {

        @Override
        public String toString() {
            return "Class_page{" +
                    "class_id='" + class_id + '\'' +
                    ", class_page_url='" + class_page_url + '\'' +
                    ", class_name='" + class_name + '\'' +
                    '}';
        }

        private String class_page_url;


        private String class_id;


        private String class_name;


        public void setClass_page_url(String class_page_url){

            this.class_page_url = class_page_url;

        }

        public String getClass_page_url(){

            return this.class_page_url;

        }

        public void setClass_id(String class_id){

            this.class_id = class_id;

        }

        public String getClass_id(){

            return this.class_id;

        }

        public void setClass_name(String class_name){

            this.class_name = class_name;

        }

        public String getClass_name(){

            return this.class_name;

        }

    }
    public class Hot {

        private String sige_url;


        private String ad_link;


        public String getAd_link() {
            return ad_link;
        }

        public void setAd_link(String ad_link) {
            this.ad_link = ad_link;
        }

        public String getSige_url() {
            return sige_url;
        }

        public void setSige_url(String sige_url) {
            this.sige_url = sige_url;
        }
    }
    public class Center {

        private String footer_url;


        private String ad_link;


        public void setFooter_url(String footer_url){

            this.footer_url = footer_url;

        }

        public String getFooter_url(){

            return this.footer_url;

        }

        public void setAd_link(String ad_link){

            this.ad_link = ad_link;

        }

        public String getAd_link(){

            return this.ad_link;

        }

    }
    public class Clas {

        private String class_url;


        private String class_id;


        private String class_name;


        public void setClass_url(String class_url){

            this.class_url = class_url;

        }

        public String getClass_url(){

            return this.class_url;

        }

        public void setClass_id(String class_id){

            this.class_id = class_id;

        }

        public String getClass_id(){

            return this.class_id;

        }

        public void setClass_name(String class_name){

            this.class_name = class_name;

        }

        public String getClass_name(){

            return this.class_name;

        }

    }
    public class HomeTravelData {

        private List<Bannner> bannner ;


        private List<Class_page> class_page ;


        private List<Hot> hot ;


        private List<Center> center ;


        private List<Clas> clas ;


        public void setBannner(List<Bannner> bannner){

            this.bannner = bannner;

        }

        public List<Bannner> getBannner(){

            return this.bannner;

        }

        public void setClass_page(List<Class_page> class_page){

            this.class_page = class_page;

        }

        public List<Class_page> getClass_page(){

            return this.class_page;

        }

        public void setHot(List<Hot> hot){

            this.hot = hot;

        }

        public List<Hot> getHot(){

            return this.hot;

        }

        public void setCenter(List<Center> center){

            this.center = center;

        }

        public List<Center> getCenter(){

            return this.center;

        }

        public void setClas(List<Clas> clas){

            this.clas = clas;

        }

        public List<Clas> getClas(){

            return this.clas;

        }

    }
    private HomeTravelData data;

    public HomeTravelData getData() {
        return data;
    }

    public void setData(HomeTravelData data) {
        this.data = data;
    }
}
