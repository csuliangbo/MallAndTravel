package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/12.
 */
public class LoginBean extends ParentBean {
    private List<LoginData> data;

    public List<LoginData> getData() {
        return data;
    }

    public void setData(List<LoginData> data) {
        this.data = data;
    }

    public class LoginData {

        private String id;


        private String grade_name;


        public void setId(String id) {

            this.id = id;

        }

        public String getId() {

            return this.id;

        }

        public void setGrade_name(String grade_name) {

            this.grade_name = grade_name;

        }

        public String getGrade_name() {

            return this.grade_name;

        }

    }
}
