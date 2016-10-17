package com.ych.mall.bean;

/**
 * Created by ych on 2016/10/13.
 */
public class UpdateBean extends ParentBean {
    public class Data {
        private String id;

        private String version_num;

        private String intro;

        private String create_time;

        private String url;

        private String app_type;

        private String version;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setVersion_num(String version_num){
            this.version_num = version_num;
        }
        public String getVersion_num(){
            return this.version_num;
        }
        public void setIntro(String intro){
            this.intro = intro;
        }
        public String getIntro(){
            return this.intro;
        }
        public void setCreate_time(String create_time){
            this.create_time = create_time;
        }
        public String getCreate_time(){
            return this.create_time;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
        public void setApp_type(String app_type){
            this.app_type = app_type;
        }
        public String getApp_type(){
            return this.app_type;
        }
        public void setVersion(String version){
            this.version = version;
        }
        public String getVersion(){
            return this.version;
        }

    }
    private Data data;
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
}
