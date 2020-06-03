package com.ysyy.biserver.mysqlclass.model;

public class User {
    public int getUser_id() {
        return user_id;
    }
    public void getUser_id(int user_id) {

        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public  int user_id;
    public String username;
    public String userpassword;
    public  int age;
    public  String sex;

}
