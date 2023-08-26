package com.example.varandev.Login;

public class Profile_model {

    private String uid;
    private String customid;
    private String gender;
    private String pic;
    private String username;
    private String age;
    private String phoneno;



    public Profile_model() {
    }



    public Profile_model(String uid, String customid, String gender, String pic, String username, String age, String phoneno) {
        this.uid = uid;
        this.customid = customid;
        this.gender = gender;
        this.pic = pic;
        this.username = username;
        this.age = age;
        this.phoneno = phoneno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCustomid() {
        return customid;
    }

    public void setCustomid(String customid) {
        this.customid = customid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
