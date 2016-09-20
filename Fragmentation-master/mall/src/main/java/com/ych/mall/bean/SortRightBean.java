package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/8.
 */
public class SortRightBean extends ParentBean {
    private List<SortRightData> data;

    public List<SortRightData> getData() {
        return data;
    }

    public void setData(List<SortRightData> data) {
        this.data = data;
    }

    public class SortRightData {

        private String id;


        private String class_name;


        public void setId(String id){

            this.id = id;

        }

        public String getId(){

            return this.id;

        }

        public void setClass_name(String class_name){

            this.class_name = class_name;

        }

        public String getClass_name(){

            return this.class_name;

        }

    }
}
