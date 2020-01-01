package com.example.marketsmodule.models;

public class Customer {
    private String location;
    private String userName;
    private String userPhoneNo;
    private String userSurname;

    @Override
    public String toString() {
        return "Customer{" +
                "location='" + location + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                ", userSurname='" + userSurname + '\'' +
                '}';
    }

    public Customer() {
    }

    public Customer(String location, String userName, String userPhoneNo, String userSurname) {
        this.location = location;
        this.userName = userName;
        this.userPhoneNo = userPhoneNo;
        this.userSurname = userSurname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }
}
