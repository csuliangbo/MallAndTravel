package com.ych.mall.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MessageBean extends ParentBean {
    private List<MessageData> data;

    public List<MessageData> getData() {
        return data;
    }

    public void setData(List<MessageData> data) {
        this.data = data;
    }

    public class MessageData {
        private String news_info;
        private String create_time;
        private String sendto;

        public String getSendto() {
            return sendto;
        }

        public void setSendto(String sendto) {
            this.sendto = sendto;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getNews_info() {
            return news_info;
        }

        public void setNews_info(String news_info) {
            this.news_info = news_info;
        }
    }
}
