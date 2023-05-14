package com.example.animal_helpers;


public class UserAccount {

    private String Uid;     // firebase Uid (고유 토큰정보)
    private String emailId;     // 이메일
    private String password;
    private String name;
    private String gender;

    public UserAccount() {
    }


    public UserAccount(String Uid, String emailId, String password, String name, String gender) {
        this.Uid = Uid;
        this.emailId = emailId;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public String getIdToken() {
        return Uid;
    }

    public void setIdToken(String idToken) {
        this.Uid = idToken;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {return gender;}

    public void setGender(String gender) {this.gender = gender;}
}