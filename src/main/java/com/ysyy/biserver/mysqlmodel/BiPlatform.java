package com.ysyy.biserver.mysqlmodel;



import java.time.LocalDateTime;

public class BiPlatform {
    public long id;
    public String number;
    public String name;
    public String nature;
    public long isSelf;
    public long state;
    public LocalDateTime addTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }


    public long getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(long isSelf) {
        this.isSelf = isSelf;
    }


    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }


    public LocalDateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }
}
