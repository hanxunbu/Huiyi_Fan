package com.huiyi.huiyifan.modle;

import java.io.Serializable;

@SuppressWarnings("serial")//去掉警告
public class Dict_Product implements Serializable {
    private Integer id;
    private String text;

    public Dict_Product() {
    }

    public Dict_Product(Integer id, String text) {
        super();
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }

}
