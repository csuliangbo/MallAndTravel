package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/14.
 */
public class UserCenterBean extends ParentBean{
    private List<UserCenterData> data;

    public List<UserCenterData> getData() {
        return data;
    }

    public void setData(List<UserCenterData> data) {
        this.data = data;
    }

    public class UserCenterData {

        private String id;


        private String mobile;


        private String integral;


        private String grade_name;


        public void setId(String id) {

            this.id = id;

        }

        public String getId() {

            return this.id;

        }

        public void setMobile(String mobile) {

            this.mobile = mobile;

        }

        public String getMobile() {

            return this.mobile;

        }

        public void setIntegral(String integral) {

            this.integral = integral;

        }

        public String getIntegral() {

            return this.integral;

        }

        public void setGrade_name(String grade_name) {

            this.grade_name = grade_name;

        }

        public String getGrade_name() {

            return this.grade_name;

        }

    }

}
