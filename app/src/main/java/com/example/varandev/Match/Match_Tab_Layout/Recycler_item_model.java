package com.example.varandev.Match.Match_Tab_Layout;

import android.content.Context;
import android.net.Uri;

public class Recycler_item_model {

    private Context context;
    private String customid;
    private String name;
    private String age;
    private String pic1;
    private int blur_id;

    public Recycler_item_model() {
    }


    public Recycler_item_model(Context context, String customid, String name, String age, String pic1, int blur_id) {
        this.context = context;
        this.customid = customid;
        this.name = name;
        this.age = age;
        this.pic1 = pic1;
        this.blur_id = blur_id;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public String getCustomid() {
        return customid;
    }

    public void setCustomid(String customid) {
        this.customid = customid;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
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

}
