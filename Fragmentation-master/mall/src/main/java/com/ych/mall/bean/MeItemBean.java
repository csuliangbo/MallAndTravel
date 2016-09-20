package com.ych.mall.bean;



/**
 * Created by ych on 2016/9/9.
 */
public class MeItemBean extends ParentBean {
    private String name;
    private int img;

    public MeItemBean(
            String name, int img
    ) {
        this.name = name;
        this.img= img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
