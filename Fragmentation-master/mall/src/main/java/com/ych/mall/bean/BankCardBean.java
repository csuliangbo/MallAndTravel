package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/14.
 */
public class BankCardBean extends ParentBean {
    private List<BankCardData> data;

    public List<BankCardData> getData() {
        return data;
    }

    public void setData(List<BankCardData> data) {
        this.data = data;
    }

    public class BankCardData {

        private String id;


        private String belong;


        private String type;


        private String card_num;


        public void setId(String id){

            this.id = id;

        }

        public String getId(){

            return this.id;

        }

        public void setBelong(String belong){

            this.belong = belong;

        }

        public String getBelong(){

            return this.belong;

        }

        public void setType(String type){

            this.type = type;

        }

        public String getType(){

            return this.type;

        }

        public void setCard_num(String card_num){

            this.card_num = card_num;

        }

        public String getCard_num(){

            return this.card_num;

        }

    }
}
