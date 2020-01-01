package com.example.customermodule.Models;

public class Markets {

    private String aboutMe;
    private String location;
    private String marketImg;
    private String marketName;
    private String marketPhoneNo;
    private String key;


    @Override
    public String toString() {
        return "Markets{" +
                "aboutMe='" + aboutMe + '\'' +
                ", location='" + location + '\'' +
                ", marketImg='" + marketImg + '\'' +
                ", marketName='" + marketName + '\'' +
                ", marketPhoneNo='" + marketPhoneNo + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public Markets() {
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMarketImg() {
        return marketImg;
    }

    public void setMarketImg(String marketImg) {
        this.marketImg = marketImg;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketPhoneNo() {
        return marketPhoneNo;
    }

    public void setMarketPhoneNo(String marketPhoneNo) {
        this.marketPhoneNo = marketPhoneNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Markets(String aboutMe, String location, String marketImg, String marketName, String marketPhoneNo, String key) {
        this.aboutMe = aboutMe;
        this.location = location;
        this.marketImg = marketImg;
        this.marketName = marketName;
        this.marketPhoneNo = marketPhoneNo;
        this.key = key;
    }
}

