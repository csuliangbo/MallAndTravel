package com.ych.mall.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class LogisticsBean extends ParentBean {
    private List<LogisticsData> data;

    public List<LogisticsData> getData() {
        return data;
    }

    public void setData(List<LogisticsData> data) {
        this.data = data;
    }

    public class LogisticsData {
        private String kuaidi_num;

        public String getKuaidi_num() {
            return kuaidi_num;
        }

        public void setKuaidi_num(String kuaidi_num) {
            this.kuaidi_num = kuaidi_num;
        }
    }
}
