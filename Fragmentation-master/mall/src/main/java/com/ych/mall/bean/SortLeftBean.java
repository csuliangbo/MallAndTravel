package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/8.
 */
public class SortLeftBean extends ParentBean {
    private List<SortLeftData> data;

    public List<SortLeftData> getData() {
        return data;
    }

    public void setData(List<SortLeftData> data) {
        this.data = data;
    }

    public class SortLeftData {

        private String id;
        private boolean isSelect;

        public boolean getSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

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
