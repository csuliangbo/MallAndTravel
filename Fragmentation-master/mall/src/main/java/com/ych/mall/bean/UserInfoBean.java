package com.ych.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ych on 2016/9/12.
 */
public class UserInfoBean extends ParentBean implements Serializable{
    private List<UserInfoData> data;

    public List<UserInfoData> getData() {
        return data;
    }

    public void setData(List<UserInfoData> data) {
        this.data = data;
    }

    public class UserInfoData implements Serializable{

        private String account_balance;


        private String realname;


        private String idcard;


        private String sex;


        private String birthday;


        private String mobile;


        private String addressid;


        private String addressrealname;


        private String addressmobile;


        private String prov;


        private String city;


        private String dist;


        private String address;


        private String status;


        private String zipcode;


        public void setAccount_balance(String account_balance) {

            this.account_balance = account_balance;

        }

        public String getAccount_balance() {

            return this.account_balance;

        }

        public void setRealname(String realname) {

            this.realname = realname;

        }

        public String getRealname() {

            return this.realname;

        }

        public void setIdcard(String idcard) {

            this.idcard = idcard;

        }

        public String getIdcard() {

            return this.idcard;

        }

        public void setSex(String sex) {

            this.sex = sex;

        }

        public String getSex() {

            return this.sex;

        }

        public void setBirthday(String birthday) {

            this.birthday = birthday;

        }

        public String getBirthday() {

            return this.birthday;

        }

        public void setMobile(String mobile) {

            this.mobile = mobile;

        }

        public String getMobile() {

            return this.mobile;

        }

        public void setAddressid(String addressid) {

            this.addressid = addressid;

        }

        public String getAddressid() {

            return this.addressid;

        }

        public void setAddressrealname(String addressrealname) {

            this.addressrealname = addressrealname;

        }

        public String getAddressrealname() {

            return this.addressrealname;

        }

        public void setAddressmobile(String addressmobile) {

            this.addressmobile = addressmobile;

        }

        public String getAddressmobile() {

            return this.addressmobile;

        }

        public void setProv(String prov) {

            this.prov = prov;

        }

        public String getProv() {

            return this.prov;

        }

        public void setCity(String city) {

            this.city = city;

        }

        public String getCity() {

            return this.city;

        }

        public void setDist(String dist) {

            this.dist = dist;

        }

        public String getDist() {

            return this.dist;

        }

        public void setAddress(String address) {

            this.address = address;

        }

        public String getAddress() {

            return this.address;

        }

        public void setStatus(String status) {

            this.status = status;

        }

        public String getStatus() {

            return this.status;

        }

        public void setZipcode(String zipcode) {

            this.zipcode = zipcode;

        }

        public String getZipcode() {

            return this.zipcode;

        }

    }
}
