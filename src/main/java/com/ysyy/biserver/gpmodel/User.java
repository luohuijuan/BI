package com.ysyy.biserver.gpmodel;

public class User {
    private String Name;
    private String Address;
    private String tt;
    public User(String name,String Address,String tt){
        this.Name = name; //     给对象赋予name值
        this.Address = Address; //    给对象赋予age值
        this.tt = tt; //    给对象赋予age值

    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

}
