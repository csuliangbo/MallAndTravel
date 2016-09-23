package com.ych.mall.bean;

import java.util.List;

/**
 * Created by ych on 2016/9/23.
 */
public class VipBean extends ParentBean {
    public class VipData{
        private String grade_name;
        private String vip_end;

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_num) {
            this.grade_name = grade_num;
        }

        public String getVip_end() {
            return vip_end;
        }

        public void setVip_end(String vip_end) {
            this.vip_end = vip_end;
        }
    }

    private List<VipData> data;

    public List<VipData> getData() {
        return data;
    }

    public void setData(List<VipData> data) {
        this.data = data;
    }
}
