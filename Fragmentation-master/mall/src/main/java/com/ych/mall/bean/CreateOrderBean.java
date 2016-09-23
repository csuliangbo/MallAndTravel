package com.ych.mall.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CreateOrderBean extends ParentBean {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
