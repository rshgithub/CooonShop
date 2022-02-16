package com.example.cocoonshop.ModelClasses;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


public class UserModel {

    private String userFullName;

    private String userNickName;

    private String userPhoneNumber;

    private String userBirthdate;

    private String userGender;

    private String userImagePath;

    public UserModel(String userFullName, String userNickName, String userPhoneNumber, String userBirthdate, String userGender, String userImagePath) {
        this.userFullName = userFullName;
        this.userNickName = userNickName;
        this.userPhoneNumber = userPhoneNumber;
        this.userBirthdate = userBirthdate;
        this.userGender = userGender;
        this.userImagePath = userImagePath;
    }

    public UserModel() {
        }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserBirthdate() {
        return userBirthdate;
    }

    public void setUserBirthdate(String userBirthdate) {
        this.userBirthdate = userBirthdate;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }
}