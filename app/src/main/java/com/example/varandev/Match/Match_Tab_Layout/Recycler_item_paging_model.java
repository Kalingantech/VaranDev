package com.example.varandev.Match.Match_Tab_Layout;

import android.content.Context;

public class Recycler_item_paging_model {

    private String customid;
    private String name;
    private String age;
    private String pic1;
    private int blur_id;
    private String uid;



    public Recycler_item_paging_model() {
    }


    public Recycler_item_paging_model( String customid, String name, String age, String pic1, int blur_id, String uid) {

        this.customid = customid;
        this.name = name;
        this.age = age;
        this.pic1 = pic1;
        this.blur_id = blur_id;
        this.uid = uid;
    }



    public String getCustomid() {
        return customid;
    }

    public void setCustomid(String customid) {
        this.customid = customid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public int getBlur_id() {
        return blur_id;
    }

    public void setBlur_id(int blur_id) {
        this.blur_id = blur_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
