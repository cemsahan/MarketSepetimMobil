package com.example.customermodule.Models;

public class Users {
    private String userName;
    private String userSurname;
    private String location;
    private String userPhoneNo;

    public Users(String userName, String userSurname, String location, String userPhoneNo) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.location = location;
        this.userPhoneNo = userPhoneNo;
    }

    public Users() {
    }

    @Override
    public String toString() {
        return "Users{" +
                "userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", location='" + location + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }
}
