package com.ych.mall.bean;

/**
 * Created by ych on 2016/9/14.
 */
public class AccountBean extends ParentBean{
    private AccountData data;

    public AccountData getData() {
        return data;
    }

    public void setData(AccountData data) {
        this.data = data;
    }

    public class AccountData {

        private String num;


        private String account_balance;


        private String integral;


        private String add_jf_currency;


        private String add_jf_limit;


        public void setNum(String num){

            this.num = num;

        }

        public String getNum(){

            return this.num;

        }

        public void setAccount_balance(String account_balance){

            this.account_balance = account_balance;

        }

        public String getAccount_balance(){

            return this.account_balance;

        }

        public void setIntegral(String integral){

            this.integral = integral;

        }

        public String getIntegral(){

            return this.integral;

        }

        public void setAdd_jf_currency(String add_jf_currency){

            this.add_jf_currency = add_jf_currency;

        }

        public String getAdd_jf_currency(){

            return this.add_jf_currency;

        }

        public void setAdd_jf_limit(String add_jf_limit){

            this.add_jf_limit = add_jf_limit;

        }

        public String getAdd_jf_limit(){

            return this.add_jf_limit;

        }

    }
}
