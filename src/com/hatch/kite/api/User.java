package com.hatch.kite.api;

/**
 * Created by vince on 9/14/13.
 */
public class User {
    private String userName;
    private String userPassword;
    private int userAge;
    private String userLocation;
    private Gender userGender;

    /**
     * Used when logging in
     * @param _un username
     */
    public User(String _un, String _pw) {
        this.userName = _un;
        this.userPassword = _pw;
    }

    public User(String userName, String userPassword, int userAge, String userLocation, Gender userGender) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.userLocation = userLocation;
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public enum Gender {
        Male, Female
    }

}
