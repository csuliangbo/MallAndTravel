package com.ych.mall.bean;

/**
 * Created by Administrator on 2016/9/24.
 */
public class CreateVipBean extends ParentBean {
    private CreateVipData data;

    public CreateVipData getData() {
        return data;
    }

    public void setData(CreateVipData data) {
        this.data = data;
    }

    public class CreateVipData {
        private String orders_num;
        private String user_id;
        private String hf_money;
        private String pay_states;

        public String getOrders_num() {
            return orders_num;
        }

        public void setOrders_num(String orders_num) {
            this.orders_num = orders_num;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getHf_money() {
            return hf_money;
        }

        public void setHf_money(String hf_money) {
            this.hf_money = hf_money;
        }

        public String getPay_states() {
            return pay_states;
        }

        public void setPay_states(String pay_states) {
            this.pay_states = pay_states;
        }
    }
}
